package ethereum.tutorials.java.ethereum.login.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ethereum.tutorials.java.ethereum.login.models.User;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    // private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        // this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name()));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // public Long getId() {
    //     return id;
    // }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    // public boolean equals(Object o) {
    //     if (this == o)
    //         return true;
    //     if (o == null || getClass() != o.getClass())
    //         return false;
    //     UserDetailsImpl user = (UserDetailsImpl) o;
    //     return Objects.equals(id, user.id);
    // }

}
