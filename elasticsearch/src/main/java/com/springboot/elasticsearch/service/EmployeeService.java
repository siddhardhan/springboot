package com.springboot.elasticsearch.service;

import java.util.Map;
import java.util.UUID;

import com.springboot.elasticsearch.document.Employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService {

	private RestHighLevelClient client;
	private ObjectMapper objectMapper;

	@Autowired
	public EmployeeService(RestHighLevelClient client, ObjectMapper objectMapper) {
		this.client = client;
		this.objectMapper = objectMapper;
	}

	public String createProfileDocument(Employee employee) throws Exception {

		UUID uuid = UUID.randomUUID();
		employee.setId(uuid.toString());

		IndexRequest indexRequest = new IndexRequest("siddhardha", "employee", employee.getId())
				.source(convertProfileDocumentToMap(employee));

		IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
		return indexResponse.getResult().name();
	}

	private Map<String, Object> convertProfileDocumentToMap(Employee employee) {
		return objectMapper.convertValue(employee, Map.class);
	}

}