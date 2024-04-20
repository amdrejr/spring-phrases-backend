package com.amdrejr.phrases.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public void create(User u) {
        if(repository.findByUsername(u.getUsername()) == null)
            repository.save(u);
    }
    
    // public void save(User u) {
    //     repository.save(u);
    // }

    public User findByUsername(String username) {
        User user = repository.findByUsername(username);
        // if(user == null)
        //     throw new RuntimeException("User not found, username: " + username);
        return user;
    }

    public Page<User> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
    
    public User findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found, id: " + id));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public User update(User u) {
        User entity = findById(u.getId());

        entity.setUsername(u.getUsername());
        entity.setPassword(u.getPassword());
        entity.setAccountNonExpired(u.getAccountNonExpired());
        entity.setAccountNonLocked(u.getAccountNonLocked());
        entity.setCredentialsNonExpired(u.getCredentialsNonExpired());
        entity.setEnabled(u.getEnabled());
        entity.setRoles(u.getRoles());
        entity.setFollowers(u.getFollowers());
        entity.setFollowing(u.getFollowing());
        entity.setPhrases(u.getPhrases());

        repository.save(entity);
        return entity;
    }

    public Page<User> findFollowersByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findFollowersByUserId(userId, pageable);
    }

    public Page<User> findFollowingByUserId(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findFollowingByUserId(userId, pageable);
    }
}
