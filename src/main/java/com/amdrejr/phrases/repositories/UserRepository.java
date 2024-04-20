package com.amdrejr.phrases.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amdrejr.phrases.entities.User;

// Repositório para manipular a tabela USERS da entidade User.
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT f FROM User u JOIN u.followers f WHERE u.id = :userId")
    Page<User> findFollowersByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT f FROM User u JOIN u.following f WHERE u.id = :userId")
    Page<User> findFollowingByUserId(@Param("userId") Long userId, Pageable pageable);
}
