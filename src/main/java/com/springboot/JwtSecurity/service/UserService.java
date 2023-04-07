package com.springboot.JwtSecurity.service;

import com.springboot.JwtSecurity.entity.User;

public interface UserService {
	User getByEmail(String email);
}
