package com.javainuse.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.javainuse.model.Employee;

public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public List<Employee> getAllEmployees() {
		return null;
	}

	@Override
	public Employee addNewEmployee(Employee employee) {
		//mongoTemplate.save(employee);
		return employee;
	}

}
