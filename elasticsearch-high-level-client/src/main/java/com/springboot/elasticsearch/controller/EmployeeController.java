package com.springboot.elasticsearch.controller;

import com.springboot.elasticsearch.document.EmployeeDocument;
import com.springboot.elasticsearch.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class EmployeeController {

    private EmployeeService service;

    @Autowired
    public EmployeeController(EmployeeService service) {

        this.service = service;
    }


    @PostMapping
    public ResponseEntity createProfile(@RequestBody List<EmployeeDocument> employeeDocumentList) throws Exception {

        return new ResponseEntity(service.createProfileDocument(employeeDocumentList), HttpStatus.CREATED);
    }

   /* @PutMapping
    public ResponseEntity updateProfile(@RequestBody EmployeeDocument document) throws Exception {

        return new ResponseEntity(service.updateProfile(document), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public EmployeeDocument findById(@PathVariable String id) throws Exception {

        return service.findById(id);
    }

    @GetMapping
    public List<EmployeeDocument> findAll() throws Exception {

        return service.findAll();
    }

    @GetMapping(value = "/search")
    public List<EmployeeDocument> search(@RequestParam(value = "technology") String technology) throws Exception {
        return service.searchByTechnology(technology);
    }

    @GetMapping(value = "/api/v1/profiles/name-search")
    public List<EmployeeDocument> searchByName(@RequestParam(value = "name") String name) throws Exception {
        return service.findProfileByName(name);
    }


    @DeleteMapping("/{id}")
    public String deleteProfileDocument(@PathVariable String id) throws Exception {

        return service.deleteProfileDocument(id);

    }*/


}
