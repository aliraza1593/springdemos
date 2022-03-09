package com.demo.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dto.UserRegistrationDTO;
import com.demo.loginentities.Role;
import com.demo.loginentities.User;
import com.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Lazy
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userrep;
	
	public UserServiceImpl(UserRepository userrep) {
	
		this.userrep = userrep;
	}

	 	  	
	@Override
	//@Transactional
	public User save(UserRegistrationDTO registrationdto) {
		
		User user= new User(registrationdto.getFirstName(),registrationdto.getLastName(),registrationdto.getEmail(),passwordEncoder.encode(registrationdto.getPassword()),Arrays.asList(new Role("ROLE_USER")));
		
		userrep.save(user);
		return user; 
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user=userrep.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("Ïnvalid Username or Password");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),mapRolesToAuthorites(user.getRoles()));
		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorites(Collection<Role> roles) {
		
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
