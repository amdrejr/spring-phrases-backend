package com.amdrejr.phrases.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.amdrejr.phrases.entities.Role;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.services.RoleService;
import com.amdrejr.phrases.services.UserService;

@Configuration
public class InitialConfig implements CommandLineRunner {
    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    // Encriptador de senhas
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        Role roleUser = new Role(1, "USER");
        Role roleAdmin = new Role(2, "ADMIN");
        roleService.save(roleUser);
        roleService.save(roleAdmin);

        User admin = createUser("admin", "admin", Arrays.asList(roleService.findById(2)));

        userService.save(admin);
    }

    private User createUser(String username, String password, List<Role> roles) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setRoles(roles);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		return user;
	}
}
