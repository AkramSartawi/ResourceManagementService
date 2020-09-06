package com.exalt.ResourceManagementService.service;

import com.exalt.ResourceManagementService.model.Server;

import java.util.List;

public interface ServerProviderService {
    void getService(int numberOfGiga);
    void deleteServers();
     List<Server> getAllServers() ;

    }
