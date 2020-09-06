package com.exalt.ResourceManagementService.controller;


import com.exalt.ResourceManagementService.model.Server;
import com.exalt.ResourceManagementService.service.ServerProviderService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/serverPool")
public class ServerController {

    private final static Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    ServerProviderService serverProviderService;

    @GetMapping("/allocate/{numberOfGiga}/Giga")
     @ResponseBody String provideService(@PathVariable("numberOfGiga") int numberOfGiga) {
        logger.info("provide service method get "+numberOfGiga+" giga");
        if (numberOfGiga > 100){
            return "Maximum number of giga that  you can provided by pool servers is 100 giga ";
        }
        if (numberOfGiga < 1 ){
            return "Minimum number of giga that you can provided by pool servers 1 giga";
        }
        // if number of giga less than 100 and larger than 0 continue
        serverProviderService.getService(numberOfGiga);
        return new String();
    }

    @GetMapping()
    public List<Server> getAll(){
        logger.info("Get All pool server method");
        return serverProviderService.getAllServers();
    }
    @DeleteMapping("/delete")
    public String deleteServers() {

        logger.info("delete servers method");
        serverProviderService.deleteServers();
        return " successfully deleted server";
    }




}
