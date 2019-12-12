package com.javainuse.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.javainuse.model.Login;
@Repository
public interface LoginRepository extends MongoRepository<Login, String> {

}
