package com.springboot.elasticsearch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.elasticsearch.model.Employee;
import com.springboot.elasticsearch.repository.Employeerepository;

@Service
public class Employeeservimpl implements Employeeserv {

	// The dao repository will use the Elastic-Search-Repository to perform the database operations.
	@Autowired
	private Employeerepository edao;

	/* (non-Javadoc)
	 * @see com.springboot.elasticsearch.service.Employeeserv#saveEmployee(java.util.List)
	 */
	@Override
	public void saveEmployee(List<Employee> employees) {
		edao.saveAll(employees);
	}

	/* (non-Javadoc)
	 * @see com.springboot.elasticsearch.service.Employeeserv#findAllEmployees()
	 */
	@Override
	public Iterable<Employee> findAllEmployees() {
		return edao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.springboot.elasticsearch.service.Employeeserv#findByDesignation(java.lang.String)
	 */
	@Override
	public List<Employee> findByDesignation(String designation) {
		return edao.findByDesignation(designation);
	}
}