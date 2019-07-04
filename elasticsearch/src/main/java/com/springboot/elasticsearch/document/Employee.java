package com.springboot.elasticsearch.document;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class Employee {

	private String id;
	private String name;
	private String designation;
	private List<Department> departmentList;

}