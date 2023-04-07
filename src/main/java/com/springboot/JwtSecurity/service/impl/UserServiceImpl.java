package com.springboot.JwtSecurity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.JwtSecurity.entity.User;
import com.springboot.JwtSecurity.repository.UserRepository;
import com.springboot.JwtSecurity.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getByEmail(String email) {
		return userRepository.findByEmail(email).get();
	}

}
