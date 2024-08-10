package com.time.demo.entity;

import com.time.demo.entity.enums.Roles;
import com.time.demo.entity.templates.AbsMainEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class Users extends AbsMainEntity implements UserDetails {
    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)   //  only lowercases & numbers
    private String username1;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 6)
    private String emailCode;

    //  +998 XX-YYY-YY-YY -> only uzbek numbers in step 1.
    @Column(unique = true, length = 13)
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private UserImage image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "createdBy", fetch = FetchType.EAGER)
    private Set<Contacts> contacts;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    //  Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
