package com.amdrejr.phrases.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.exceptions.customExceptions.PhrasesErrorException;
import com.amdrejr.phrases.repositories.PhraseRepository;

@Service
public class PhraseService {

    @Autowired
    PhraseRepository repository;

    public void save(Phrase p) {
        repository.save(p);
    }

    public List<Phrase> findAll() {
        return repository.findAll();
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
    
    public List<Phrase> findByUserId(Long userId) {
        List<Phrase> phrases = repository.findAll().stream()
            .filter(p -> p.getUser().getId() == userId)
            .toList();
        return phrases;
    }
}
