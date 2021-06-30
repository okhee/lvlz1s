package kr.co.okheeokey.user.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Embedded
    @EqualsAndHashCode.Exclude
    private UserRoles userRoles;

    @Column(nullable = false)
    private Boolean enabled;

    public User(String name, String password, UserRoles userRoles) {
        this.name = name;
        this.password = password;
        this.userRoles = userRoles;
        this.enabled = true;
    }

    public Boolean isAdmin() {
        return userRoles.isAdmin();
    }

    public String getRole() {
        return userRoles.getHighestRole().name();
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.getAuthorities();
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
        return this.enabled;
    }
}
