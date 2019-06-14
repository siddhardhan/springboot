package com.springboot.elasticsearch.service;

import java.util.List;

import com.springboot.elasticsearch.model.Employee;

public interface Employeeserv {

	/**
	 * Method to save the collection of employees in the database.
	 * @param employees
	 */
	public void saveEmployee(List<Employee> employees);

	/**
	 * Method to fetch all employees from the database.
	 * @return
	 */
	public Iterable<Employee> findAllEmployees();

	/**
	 * Method to fetch the employee details on the basis of designation.
	 * @param designation
	 * @return
	 */
	public List<Employee> findByDesignation(String designation);	
}