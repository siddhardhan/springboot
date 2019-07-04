package com.springboot.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.elasticsearch.document.Employee;
import com.springboot.elasticsearch.service.EmployeeService;

@RestController
@RequestMapping(value= "/employee")
public class EmployeeController {

	private EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {

		this.employeeService = employeeService;
	}


	@PostMapping(value= "/saveemployees")
	public ResponseEntity createProfile(@RequestBody Employee employee) throws Exception {

		return new ResponseEntity(employeeService.createProfileDocument(employee), HttpStatus.CREATED);
	}

}