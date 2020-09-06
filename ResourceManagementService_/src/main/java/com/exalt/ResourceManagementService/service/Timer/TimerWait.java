package com.exalt.ResourceManagementService.service.Timer;

import com.exalt.ResourceManagementService.model.Server;
import com.exalt.ResourceManagementService.repository.ServerRepository;

import java.util.Map;

public class TimerWait implements Runnable {

    ServerRepository serverRepository;
    Server server;
    Map<Integer, Integer> localServers;
    int numberOfGigas;
    Thread thread;

    public TimerWait() {
    }

    public TimerWait(Server server, ServerRepository serverRepository, Map<Integer, Integer> localServers, int numberOfGigas) {
        this.serverRepository = serverRepository;
        this.server = server;
        this.localServers = localServers;
        this.numberOfGigas = numberOfGigas;
        thread = new Thread(this, "server id :" + server.getId());
        thread.start();
    }


    @Override
    public void run() {


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (server) {
            server.setActive(true);
            synchronized (serverRepository) {
                server.setCapacity(localServers.get(server.getId()));
                localServers.remove(server.getId());
                serverRepository.save(server);
            }
        }
    }
}
