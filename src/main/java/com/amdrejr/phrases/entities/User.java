package com.amdrejr.phrases.entities;


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

// Entidade User, será usado seus params para login.
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "account_non_expired")
    @JsonIgnore
    private Boolean accountNonExpired;
    @Column(name = "account_non_locked")
    @JsonIgnore
    private Boolean accountNonLocked;
    @Column(name = "credentials_non_expired")
    @JsonIgnore
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id") )
    @JsonIgnore
    private List<Role> roles;    

    public User() { }

    public User(String username, String password, Integer roleId) { 
        this.username = username;
        this.password = password;
        this.accountNonExpired = true;
        this.accountNonLocked = true;   
        this.credentialsNonExpired = true;
        this.enabled = true;
        
        this.roles = List.of(new Role(roleId));
    }

    public User(Long id, String username, String password, Boolean accountNonExpired, Boolean accountNonLocked,
            Boolean credentialsNonExpired, Boolean enabled, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.accountNonExpired = u.getAccountNonExpired();
        this.accountNonLocked = u.getAccountNonLocked();
        this.credentialsNonExpired = u.getCredentialsNonExpired();
        this.enabled = u.getEnabled();
        this.roles = u.getRoles();
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }
    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }
    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    // UserDetails métodos
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return List.of(new SimpleGrantedAuthority("ADMIN"));
        return this.roles;
    }
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", enabled=" + enabled + ", roles=" + roles + "]";
    }

}
