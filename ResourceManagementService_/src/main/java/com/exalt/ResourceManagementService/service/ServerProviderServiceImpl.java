package com.exalt.ResourceManagementService.service;

import com.exalt.ResourceManagementService.id.GenerateID;
import com.exalt.ResourceManagementService.model.Server;
import com.exalt.ResourceManagementService.repository.GenerateIDRepository;
import com.exalt.ResourceManagementService.repository.ServerRepository;
import com.exalt.ResourceManagementService.service.Timer.TimerWait;
import com.exalt.ResourceManagementService.service.process.ThreadRunServerProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServerProviderServiceImpl implements ServerProviderService {

    private final static Logger logger = LoggerFactory.getLogger(ServerProviderServiceImpl.class);
    Map<Integer, Integer> localServers = new HashMap<>();//(server id ,capacity)
    @Autowired
    ServerRepository serverRepository;
    @Autowired
    GenerateIDRepository generateIDRepository;
    Server server;
    ThreadRunServerProcess threadRunServerProcess;
    TimerWait timerWait;
    GenerateID generateID;
    boolean isWaiting = false;


    @Override
    public void getService(int numberOfGiga) {
        logger.info("Get service method start in ServerProviderServiceImpl");
        server = FindServerHasAvailableSpace(numberOfGiga);
        if (server == null) {
            logger.info("There is no enough free space in available pool server");
            isWaiting = true;
            server = createServer(numberOfGiga);
            timerWait = new TimerWait(server, serverRepository, localServers, numberOfGiga);
        } else if (!server.isActive()) {
            logger.info("There is enough free space in available locally server that not active yet");
            isWaiting = true;
        } else {
            logger.info("There is enough free space in available pool server already active");
            isWaiting = false;
        }
        threadRunServerProcess = new ThreadRunServerProcess(numberOfGiga, isWaiting, serverRepository, server);
    }

    public void deleteServers() {
        localServers.clear();
        serverRepository.deleteAll();
    }

    private Server createServer(int capacity) {
        Server server = null;
        synchronized (generateIDRepository) {
            List<GenerateID> generateIDs = new ArrayList<>();
            generateIDRepository.findAll().forEach(id -> generateIDs.add(id));
            if (generateIDs.size() == 0) {
                generateID = new GenerateID();
                synchronized (generateID) {
                    generateID.setId(1);
                    generateIDRepository.save(generateID);
                }
            } else {
                    generateID = generateIDs.get(0);
            }
            logger.info("Create Server method creates new server with id : " + generateID.getId());
            server = new Server(generateID.getId());
            int id = generateID.getId() + 1;
            generateIDRepository.deleteAll();
            generateID.setId(id);
            generateIDRepository.save(generateID);
            serverRepository.save(server);
            localServers.put(server.getId(), capacity);
            logger.info("server with id : "+server.getId()+" Put in local servers map with capacity: "+localServers.get(server.getId()));
        }
        return server;
    }


    private synchronized Server FindServerHasAvailableSpace(int space) {
        List<Server> servers = getAllServers();
        for (Server server : servers) {
            int freeSpace = 100 - server.getCapacity();
            //1- if free space 100, this means server still locally and not active so check locally servers to check capacity
                logger.info("in find available server map server :"+localServers.get(server.getId()));
            if ((!server.isActive())&& (server.getCapacity() == 0) && localServers.containsKey(server.getId())) {
                logger.info("inside server map server :"+localServers.get(server.getId()));
                int capacity = localServers.get(server.getId());
                int localFreeSpace = 100 - capacity;
                if ((localFreeSpace >= space)) {
                    // update new capacity in local server since we add new capacity for it while it waiting to active state
                    localServers.put(server.getId(), localServers.get(server.getId()) + space);
                    return server;
                }
            } else if (freeSpace >= space) {
                return server;
            }

        }
        return null;// return null
    }


    public List<Server> getAllServers() {
        List<Server> servers = new ArrayList<>();
        serverRepository.findAll().forEach(server -> servers.add(server));
        return servers;
    }


}
