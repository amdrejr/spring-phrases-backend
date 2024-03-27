package com.amdrejr.phrases.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.amdrejr.phrases.dto.AuthResponse;
import com.amdrejr.phrases.dto.UserDTO;
import com.amdrejr.phrases.entities.Role;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.UsernameAlreadyExistsExceprion;
import com.amdrejr.phrases.security.UserCredentials;
import com.amdrejr.phrases.services.RoleService;
import com.amdrejr.phrases.services.UserService;
import com.amdrejr.phrases.services.security.AuthenticationService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationService authenticationService;

    // Encriptador de senhas
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {

        // isso está diretamente ligado com "User findByUsername(String username);"
        // de UserRepository
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // se admin, retorna todos os usuários
        if(actualUser.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
            return ResponseEntity.ok().body(userService.findAll());
        } else { // se não, retorna apenas o usuário logado
            return ResponseEntity.ok().body(List.of(actualUser));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping // testar se está funcionando corretamente
    public ResponseEntity<AuthResponse> create(@RequestBody @NonNull UserDTO user) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userService.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsExceprion("Username already exists.");
        }

        User newUser = new User();

        if(actualUser.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
            newUser.setUsername(user.getUsername());
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setRoles(List.of(roleService.findById(user.getRoleId())));
            newUser.setAccountNonExpired(true);
            newUser.setAccountNonLocked(true);
            newUser.setCredentialsNonExpired(true);
            newUser.setEnabled(true);
        } else {
            // apenas Users Admins podem criar usuários com roles além de ROLE_USER
            newUser.setUsername(user.getUsername());
            newUser.setPassword(encoder.encode(user.getPassword()));
            newUser.setRoles(List.of(roleService.findById(10))); // 10 = ROLE_USER
            newUser.setAccountNonExpired(true);
            newUser.setAccountNonLocked(true);
            newUser.setCredentialsNonExpired(true);
            newUser.setEnabled(true);
        }
        
        userService.save(newUser);

        UserCredentials userCredentials = new UserCredentials(newUser.getUsername(), user.getPassword());
        String token = authenticationService.signin(userCredentials);

        return ResponseEntity.created(null).body(new AuthResponse(token));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}") // adm atualizar role e enabled de outros usuários
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO obj) {
        User user = userService.findById(id);
        
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.findById(obj.getRoleId()));
        user.setRoles(roles);
        
        user.setEnabled(obj.getEnabled());

        userService.update(user);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping // atualizar própria senha
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO obj) {
        User actualUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();

        user = userService.findById(actualUser.getId());
        user.setPassword(encoder.encode(obj.getPassword()));

        userService.update(user);

        return ResponseEntity.ok().build();
    }
}


// Bloqueio de endpoints:
// 
// Só vai funcionar com: @PreAuthorize("hasAuthority('ADMIN')")
// @PreAuthorize("hasRole('ADMIN')") NÃO FUNCIONA.