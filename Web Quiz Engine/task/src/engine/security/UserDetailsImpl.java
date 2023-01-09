package engine.security;

import engine.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    Long id;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesandAuthorities;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.rolesandAuthorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesandAuthorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
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
        return true;
    }

}
