package com.exalt.ResourceManagementService.service.process;

import com.exalt.ResourceManagementService.model.Server;
import com.exalt.ResourceManagementService.repository.ServerRepository;
import com.exalt.ResourceManagementService.service.ServerProviderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ThreadRunServerProcess implements Runnable{

    private final static Logger logger = LoggerFactory.getLogger(ThreadRunServerProcess.class);
    ServerRepository serverRepository;
    Server server;
    private int numberOfGigas;
    Thread thread;
    boolean isWaiting;





    public ThreadRunServerProcess(int numberOfGigas, boolean isNewCreation, ServerRepository serverRepository, Server server) {
        this.numberOfGigas = numberOfGigas;
        this.isWaiting = isNewCreation;
        this.serverRepository = serverRepository;
        this.server = server;
        logger.info("Construction Of Running Process In Thread in in creation state");
        thread = new Thread(this,"Thread for server id "+server.getId());
        thread.start();
    }


    @Override
    public void run() {
        logger.info("Thread : "+thread.getName()+" in running state");
        synchronized (serverRepository){
            if(isWaiting){
                waitServer(server);
            }else{
                // if server not waiting we need to update capacity.
                server.setCapacity(server.getCapacity()+numberOfGigas);
                serverRepository.save(server);
            }
            logger.info("Finish Allocation memory for thread name : "+thread.getName());
        }
    }
    private void waitServer(Server server) {
        while (!server.isActive()) {

        }
    }


}
