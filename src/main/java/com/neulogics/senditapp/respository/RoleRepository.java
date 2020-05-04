package com.neulogics.senditapp.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neulogics.senditapp.models.ERole;
import com.neulogics.senditapp.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(ERole name);

}
