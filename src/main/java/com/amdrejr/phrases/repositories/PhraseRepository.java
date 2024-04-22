package com.amdrejr.phrases.repositories;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;

public interface PhraseRepository extends JpaRepository<Phrase, Long>{ 
    
    @Query("SELECT p FROM Phrase p WHERE p.user.id = :userId")
    Page<Phrase> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Phrase p WHERE p.user IN (:followingUsers)")
    Page<Phrase> findPhrasesByFollowingUsers(@Param("followingUsers") Set<User> followingUsers, Pageable pageable);

    // @Query("SELECT p.likedByUsers FROM Phrase p WHERE p.id = :phraseId")
    @Query("SELECT DISTINCT u FROM Phrase p JOIN p.likedByUsers u WHERE p.id = :phraseId")
    Page<User> findUsersLikedByPhraseId(@Param("phraseId") Long phraseId, Pageable pageable);
}