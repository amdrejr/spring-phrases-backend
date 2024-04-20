package com.amdrejr.phrases.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amdrejr.phrases.dto.UserDTO;
import com.amdrejr.phrases.entities.Role;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.UsernameAlreadyExistsExceprion;
import com.amdrejr.phrases.services.RoleService;
import com.amdrejr.phrases.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    
    // Encriptador de senhas
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<User> usersPage = userService.findAll(page, size);
        Page<UserDTO> userDTOPage = usersPage.map(user -> new UserDTO(user));

        return ResponseEntity.ok().body(userDTOPage);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO dto = new UserDTO( userService.findById(user.getId()) );

        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(new UserDTO(userService.findById(id)));
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(new UserDTO(userService.findByUsername(username)));
    }

    @PostMapping // testar se está funcionando corretamente
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> create(@RequestBody @NonNull User user) {
        if(userService.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsExceprion("Username already exists.");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setRoles(List.of(roleService.findById(user.getRoles().get(0).getId())));
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        
        userService.create(newUser);

        return ResponseEntity.ok().body(new UserDTO(newUser));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}") // adm atualizar role e enabled de outros usuários
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody User obj) {
        User user = userService.findById(id);
        
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findById(obj.getRoles().get(0).getId()));
        user.setRoles(roles);
        
        user.setEnabled(obj.getEnabled());

        userService.update(user);

        return ResponseEntity.ok().body(new UserDTO(obj));
    }

    @PutMapping // atualizar própria senha
    public ResponseEntity<UserDTO> update(@RequestBody User obj) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();

        user = userService.findById(actualUser.getId());
        user.setPassword(encoder.encode(obj.getPassword()));

        userService.update(user);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/follow")
    public ResponseEntity<Boolean> follow(@PathVariable Long id) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findById(id);

        if(actualUser.getId() == user.getId()) {
            return ResponseEntity.badRequest().build();
        }

        Boolean isFollowing;

        if(actualUser.getFollowing().contains(user)) {
            actualUser.getFollowing().remove(user);
            isFollowing = false;
        } else {
            actualUser.getFollowing().add(user);
            isFollowing = true;
        }
        
        userService.update(actualUser);

        return ResponseEntity.ok().body(isFollowing);
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<Page<Map<String, Object>>> getFollowers(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    
    ) {
        Page<User> followers = userService.findFollowersByUserId(id, page, size);
        
        Page<Map<String, Object>> followersDTO = followers.map(follower -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", follower.getId());
            map.put("username", follower.getUsername());
            return map;
        });

        return ResponseEntity.ok().body(followersDTO);
    }

    @GetMapping("/following/{id}")
    public ResponseEntity<Page<Map<String, Object>>> getFollowing(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<User> following = userService.findFollowingByUserId(id, page, size);
        
        Page<Map<String, Object>> followingDTO = following.map(followingUser -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", followingUser.getId());
            map.put("username", followingUser.getUsername());
            return map;
        });

        return ResponseEntity.ok().body(followingDTO);
    }
}


// Bloqueio de endpoints:
// 
// Só vai funcionar com: @PreAuthorize("hasAuthority('ADMIN')")
// @PreAuthorize("hasRole('ADMIN')") NÃO FUNCIONA.