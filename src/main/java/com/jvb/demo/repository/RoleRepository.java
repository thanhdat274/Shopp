package com.jvb.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jvb.demo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
