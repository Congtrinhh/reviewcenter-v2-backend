package com.trinhquycong.restloginreviewcenter.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinhquycong.restloginreviewcenter.model.Role;


public interface RoleRepo extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
