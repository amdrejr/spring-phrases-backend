package com.amdrejr.phrases.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.entities.Role;
import com.amdrejr.phrases.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repository;

    public void save(Role r) {
        repository.save(r);
    }

    public Role findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found, id: " + id));
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public Role update(Role r) {
        Role entity = findById(r.getId());

        entity.setName(r.getName());

        repository.save(entity);
        return entity;
    }
    
}
