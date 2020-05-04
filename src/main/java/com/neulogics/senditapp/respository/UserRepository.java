package com.neulogics.senditapp.respository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.neulogics.senditapp.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

}
