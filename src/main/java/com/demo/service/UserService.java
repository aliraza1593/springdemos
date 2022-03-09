package com.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.demo.dto.UserRegistrationDTO;
import com.demo.loginentities.User;

public interface UserService extends UserDetailsService{

	User save(UserRegistrationDTO registrationdto);
	
	
}
