package com.javainuse.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.dao.EmployeeRepository;
import com.javainuse.model.Employee;

@RestController
@CrossOrigin
public class TestController {

	public TestController(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	private List<Employee> employees = createList();

	private EmployeeRepository employeeRepository;

	@RequestMapping(value = "/getAllEmployees", method = RequestMethod.GET)
	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Employee addNewEmployee(@RequestBody Employee employee) {
		return employeeRepository.save(employee);

	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public List<Employee> firstPage(@RequestHeader("Authorization") String token) {
		if (JwtTokenUtil.verifyToken(token)) {
			return employees;
		}
		return null;

	}

	@DeleteMapping(path = { "/{id}" })
	public Employee delete(@PathVariable("id") int id) {
		Employee deletedEmp = null;
		for (Employee emp : employees) {
			if (emp.getEmpId().equals(id)) {
				employees.remove(emp);
				deletedEmp = emp;
				break;
			}
		}
		return deletedEmp;
	}

	@PostMapping
	public Employee create(@RequestBody Employee user) {
		employees.add(user);
		System.out.println(employees);
		return user;
	}

	private static List<Employee> createList() {
		List<Employee> tempEmployees = new ArrayList<>();
		Employee emp1 = new Employee();
		emp1.setName("emp1");
		emp1.setDesignation("manager");
		emp1.setEmpId(1);
		emp1.setSalary(3000);

		Employee emp2 = new Employee();
		emp2.setName("emp2");
		emp2.setDesignation("developer");
		emp2.setEmpId(2);
		emp2.setSalary(3000);
		tempEmployees.add(emp1);
		tempEmployees.add(emp2);
		return tempEmployees;
	}

}