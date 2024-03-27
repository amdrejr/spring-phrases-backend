package com.amdrejr.phrases.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amdrejr.phrases.entities.User;

// Repositório para manipular a tabela USERS da entidade User.
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
