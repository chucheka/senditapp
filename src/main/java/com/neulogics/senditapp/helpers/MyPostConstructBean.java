//package com.neulogics.senditapp.helpers;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.neulogics.senditapp.models.ERole;
//import com.neulogics.senditapp.models.Role;
//import com.neulogics.senditapp.respository.RoleRepository;
//
//@Component
//public class MyPostConstructBean {
//	@Autowired
//	private RoleRepository roleRepo;
//	
//	@PostConstruct
//	  void postConstruct(){
//	   roleRepo.save(new Role(ERole.ROLE_USER));
//	   roleRepo.save(new Role(ERole.ROLE_ADMIN));
//	   roleRepo.save(new Role(ERole.ROLE_RIDER));
//	  }
//
//}
