package com.amdrejr.phrases.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.PhrasesErrorException;
import com.amdrejr.phrases.repositories.PhraseRepository;

@Service
public class PhraseService {

    @Autowired
    PhraseRepository repository;

    public void save(Phrase p) {
        repository.save(p);
    }

    public Page<Phrase> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Page<Phrase> findPhrasesByFollowingUsers(Set<User> followingUsers, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findPhrasesByFollowingUsers(followingUsers, pageable);
    }

    public Phrase findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new PhrasesErrorException("Phrase not found, id: " + id));
    }

    public void delete(Phrase p) {
        repository.delete(p);
    }

    public Phrase update(Phrase p) {
        Phrase entity = findById(p.getId());

        entity.setText(p.getText());
        entity.setDate(p.getDate());

        repository.save(entity);
        return entity;
    }
    
    public Page<Phrase> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByUserId(userId, pageable);
    }
}
