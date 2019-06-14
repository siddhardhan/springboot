package com.springboot.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.stereotype.Component;
import java.util.List;

@Component

// Elastic search annotation.
@Document(indexName= "newemployees", type= "employee")
public class Employee {

	@Id
	private String id;
	private String name;
	private String designation;
	@Field(type = FieldType.Nested, includeInParent = true)
	private List<Department> departmentList;

	public Employee() { }

	public Employee(String id, String name, String designation, List<Department> departmentList) {
		this.id = id;
		this.name = name;
		this.designation = designation;
		this.departmentList = departmentList;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", designation='" + designation + '\'' +
				", departmentList=" + departmentList +
				'}';
	}
}