package com.amdrejr.phrases.controllers;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amdrejr.phrases.dto.PhraseDTO;
import com.amdrejr.phrases.entities.Phrase;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.PhrasesErrorException;
import com.amdrejr.phrases.services.PhraseService;

@RestController
@RequestMapping("/phrases")
public class PhraseController {
    @Autowired
    private PhraseService phraseService;

    @GetMapping
    public ResponseEntity<List<Phrase>> getAllPhrases() {
        return ResponseEntity.ok().body(phraseService.findAll());
    }

    @GetMapping("/{id}") // phrases by user id
    public ResponseEntity<List<Phrase>> getPhrasesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok().body(phraseService.findByUserId(id));
    }

    @GetMapping("/my-phrases")
    public ResponseEntity<List<Phrase>> getMyPhrases() {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(phraseService.findByUserId(actualUser.getId()));
    }
    
    @PostMapping
    public ResponseEntity<Phrase> createPhrase(@RequestBody @NonNull PhraseDTO p) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase newPhrase = new Phrase();

        newPhrase.setUser(actualUser);
        newPhrase.setText(p.getText());
        newPhrase.setDate(Date.from(Instant.now()));
        
        phraseService.save(newPhrase);
        return ResponseEntity.ok().body(newPhrase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhrase(@PathVariable Long id) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(id);

        if(phrase.getUser().getId() != actualUser.getId()) {
            throw new PhrasesErrorException("You can't delete a phrase that is not yours");
        }

        phraseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Phrase> updatePhrase(@RequestBody @NonNull PhraseDTO p) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(p.getId());

        if(phrase.getUser().getId() != actualUser.getId()) {
            throw new PhrasesErrorException("You can't update a phrase that is not yours");
        }

        phrase.setText(p.getText());
        phrase.setDate(Date.from(Instant.now()));

        phraseService.save(phrase);
        return ResponseEntity.ok().body(phrase);
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<Phrase> likePhrase(@PathVariable Long id) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(id);

        if(phrase.getUsersLiked().contains(actualUser)) {
            phrase.getUsersLiked().remove(actualUser);
        } else {
            phrase.getUsersLiked().add(actualUser);
        }

        phraseService.save(phrase);
        return ResponseEntity.ok().body(phrase);
    }

}
