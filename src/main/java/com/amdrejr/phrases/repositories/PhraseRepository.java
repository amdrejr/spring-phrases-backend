package com.amdrejr.phrases.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amdrejr.phrases.entities.Phrase;

public interface PhraseRepository extends JpaRepository<Phrase, Long>{ 
}
