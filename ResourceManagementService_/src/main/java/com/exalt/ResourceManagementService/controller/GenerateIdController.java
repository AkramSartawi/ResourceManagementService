package com.exalt.ResourceManagementService.controller;

import com.exalt.ResourceManagementService.id.GenerateID;
import com.exalt.ResourceManagementService.repository.GenerateIDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/id")
public class GenerateIdController {
    private final static Logger logger = LoggerFactory.getLogger(GenerateIdController.class);

    @Autowired
    GenerateIDRepository generateIDRepository;

    @DeleteMapping("/delete")
    public String deleteID() {

        logger.info("delete all id method");
        generateIDRepository.deleteAll();
        return " successfully deleted server";
    }

    @GetMapping("/all")
    public @ResponseBody
    List<GenerateID> getAllIDs(){
        List<GenerateID> generateIDS = new ArrayList<>();
        generateIDRepository.findAll().forEach(id-> generateIDS.add(id));
        return  generateIDS;
    }


}
