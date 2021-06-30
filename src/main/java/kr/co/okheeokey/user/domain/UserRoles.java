package kr.co.okheeokey.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRoles {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID")
    )
    @Enumerated(EnumType.STRING)
    private Set<UserRole> userRoles = new HashSet<>();

    public UserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream().map(UserRole::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public Boolean isAdmin() {
        return userRoles.contains(UserRole.ADMIN);
    }

    public UserRole getHighestRole() {
        return Stream.of(UserRole.values())
                .filter(userRoles::contains)
                .findFirst()
                .orElse(UserRole.USER);
    }
}
