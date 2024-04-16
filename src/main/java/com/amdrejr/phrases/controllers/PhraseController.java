package com.amdrejr.phrases.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/phrases")
public class PhraseController {
    @Autowired
    private PhraseService phraseService;

    @GetMapping
    public ResponseEntity<List<PhraseDTO>> getAllPhrases() {

        List<PhraseDTO> phrases = new ArrayList<>();

        phraseService.findAll().stream().forEach(phrase -> {
            phrases.add(new PhraseDTO(phrase));
        });

        return ResponseEntity.ok().body(phrases);
    }

    @GetMapping("/{id}") // phrases by user id
    public ResponseEntity<List<PhraseDTO>> getPhrasesByUserId(@PathVariable Long id) {
        List<PhraseDTO> phrases = new ArrayList<>();

        phraseService.findByUserId(id).stream().forEach(phrase -> {
            phrases.add(new PhraseDTO(phrase));
        });

        return ResponseEntity.ok().body(phrases);
    }

    @GetMapping("/my-phrases")
    public ResponseEntity<List<PhraseDTO>> getMyPhrases() {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PhraseDTO> phrases = new ArrayList<>();

        phraseService.findByUserId(actualUser.getId()).stream().forEach(phrase -> {
            phrases.add(new PhraseDTO(phrase));
        });

        return ResponseEntity.ok().body(phrases);
    }
    
    @PostMapping
    public ResponseEntity<PhraseDTO> createPhrase(@RequestBody PhraseDTO p) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase newPhrase = new Phrase();

        newPhrase.setUser(actualUser);
        newPhrase.setText(p.getText());
        newPhrase.setDate(Date.from(Instant.now()));
        
        phraseService.save(newPhrase);
        return ResponseEntity.ok().body(new PhraseDTO(newPhrase));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletePhrase(@PathVariable Long id) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(id);

        if(phrase.getUser().getId() != actualUser.getId()) {
            throw new PhrasesErrorException("You can't delete a phrase that is not yours");
        }

        phraseService.delete(phrase);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhraseDTO> updatePhrase(@RequestBody @NonNull PhraseDTO p) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(p.getId());

        if(phrase.getUser().getId() != actualUser.getId()) {
            throw new PhrasesErrorException("You can't update a phrase that is not yours");
        }

        phrase.setText(p.getText());
        phrase.setDate(Date.from(Instant.now()));

        phraseService.save(phrase);
        return ResponseEntity.ok().body(new PhraseDTO(phrase));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<PhraseDTO> likePhrase(@PathVariable Long id) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Phrase phrase = phraseService.findById(id);

        if(phrase.getLikedByUsers().contains(actualUser)) {
            phrase.getLikedByUsers().remove(actualUser);
        } else {
            phrase.getLikedByUsers().add(actualUser);
        }

        phraseService.save(phrase);

        return ResponseEntity.ok().body(new PhraseDTO(phrase));
    }

    @GetMapping("/following")
    public ResponseEntity<List<PhraseDTO>> getFollowingPhrases() {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PhraseDTO> followingPhrases = actualUser.getFollowing().stream()
            .flatMap(u -> u.getPhrases().stream())
            .map(PhraseDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(followingPhrases);
    }

}
