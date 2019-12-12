package com.javainuse.controller;

import java.util.Objects;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javainuse.config.JwtTokenUtil;
import com.javainuse.dao.LoginRepository;
import com.javainuse.exception.CustomErrorType;
import com.javainuse.model.JwtResponse;
import com.javainuse.model.Login;
import com.mongodb.MongoClient;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
	
	private LoginRepository loginRepository;
	
	public JwtAuthenticationController(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	public MongoTemplate getMongoTemplate() {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
				new MongoClient(), "mydb");
		return new MongoTemplate(mongoDbFactory);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody Login jwtLoginRequest) throws Exception {
		String token = null;
		boolean authenticationResult = authenticate(
				jwtLoginRequest.getUsername(), jwtLoginRequest.getPassword());
		if (authenticationResult == true) {
			token = JwtTokenUtil.createJsonWebToken(
					jwtLoginRequest.getUsername(), 1l);
			System.out.println("Token--------" + token);
		}
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Login> employeeRegistration(@RequestBody Login jwtLoginRequest){
		boolean userExists=false;
		try {
			userExists = authenticate(jwtLoginRequest.getUsername(), jwtLoginRequest.getPassword());
		} catch (Exception e) {
			System.out.println("Exception Occured while authentication" + e.getMessage());
			
		}
		if(userExists){
			return new ResponseEntity(new CustomErrorType("Unable to create. A User with name " + 
					jwtLoginRequest.getUsername() + " already exist."),HttpStatus.CONFLICT);
		}
		
		Login registeredEmployee = loginRepository.save(jwtLoginRequest);
		if(ObjectUtils.isEmpty(registeredEmployee)){
			return new ResponseEntity(new CustomErrorType("Unable to create. A User with name " + 
					jwtLoginRequest.getUsername() + " "),HttpStatus.OK);
		}
		return  ResponseEntity.status(HttpStatus.OK).body(registeredEmployee);
		
	}

	private boolean authenticate(String username, String password)
			throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		boolean flag = false;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("username").is(username));
			System.out
					.println("Query Generated----------->" + query.toString());
			MongoTemplate mongoTemplate = getMongoTemplate();
			if (mongoTemplate != null) {
				Login login = mongoTemplate.findOne(query, Login.class);
				if (login != null && password.equals(login.getPassword())) {
					flag = true;
				}
			}
			return flag;
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
