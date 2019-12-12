package com.javainuse.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javainuse.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Integer> {

}
