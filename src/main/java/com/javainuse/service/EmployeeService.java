package com.javainuse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javainuse.model.Employee;

@Service
public interface EmployeeService {
 List<Employee> getAllEmployees();
 Employee addNewEmployee(Employee employee);
}
