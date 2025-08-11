package com.microsorvices.demo.elastic.query.service.security;

import com.microsorvices.demo.elastic.query.service.service.QueryUserService;
import com.microsorvices.demo.elastic.query.service.transformer.UserPermissionsToUserDetailsTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MastodonQueryUserDetailsService implements UserDetailsService {

    private final QueryUserService queryUserService;
    private final UserPermissionsToUserDetailsTransformer userPermissionsToUserDetailsTransformer;

    public MastodonQueryUserDetailsService(QueryUserService queryUserService,
                                           UserPermissionsToUserDetailsTransformer userPermissionsToUserDetailsTransformer) {
        this.queryUserService = queryUserService;
        this.userPermissionsToUserDetailsTransformer = userPermissionsToUserDetailsTransformer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var permissions = queryUserService.getUserPermissionsByUsername(username);
        var mappedPermissions = permissions.map(userPermissionsToUserDetailsTransformer::getUserDetails);
        return mappedPermissions.orElseThrow(() -> new UsernameNotFoundException("There is no user with name " + username));
    }
}
