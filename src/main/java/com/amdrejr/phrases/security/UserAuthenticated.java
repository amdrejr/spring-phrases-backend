package com.amdrejr.phrases.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.amdrejr.phrases.entities.User;

public class UserAuthenticated implements UserDetails {
    private final User user;
  
    public UserAuthenticated(User user) {
      this.user = user;
    }
  
    @Override
    public String getUsername() {
      return user.getUsername();
    }
  
    @Override
    public String getPassword() {
      return user.getPassword();
    }
  
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return user.getAuthorities();
    }
  
    @Override
    public boolean isAccountNonExpired() {
      return user.isAccountNonExpired();
    }
  
    @Override
    public boolean isAccountNonLocked() {
      return user.isAccountNonExpired();
    }
  
    @Override
    public boolean isCredentialsNonExpired() {
      return user.isCredentialsNonExpired();
    }
  
    @Override
    public boolean isEnabled() {
      return user.isEnabled();
    }
  
  }