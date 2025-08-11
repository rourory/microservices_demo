package com.microsorvices.demo.elastic.query.service.security;

import com.microsorvices.demo.elastic.query.service.Constants;
import com.microsorvices.demo.elastic.query.service.data.entity.UserPermission;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

@Builder
@Getter
public class MastodonQueryUser implements UserDetails {

    private String username;
    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, PermissionType> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return Constants.NA;
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
}
