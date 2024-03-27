package com.amdrejr.phrases.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amdrejr.phrases.entities.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> { }
