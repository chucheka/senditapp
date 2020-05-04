package com.neulogics.senditapp.utils;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.neulogics.senditapp.models.User;
import com.neulogics.senditapp.respository.UserRepository;

@Component
public class UserUtil {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public User getCurrentUser(HttpServletRequest req) {
		String headerAuth = req.getHeader("Authorization");
		String token = headerAuth.substring(7,headerAuth.length());
		User user = userRepo.findByUsername(jwtUtils.getUserNameFromJwtToken(token));
		return user;
	}
	
}
