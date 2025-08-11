package com.microsorvices.demo.elastic.query.service.transformer;

import com.microsorvices.demo.elastic.query.service.data.entity.UserPermission;
import com.microsorvices.demo.elastic.query.service.security.MastodonQueryUser;
import com.microsorvices.demo.elastic.query.service.security.PermissionType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPermissionsToUserDetailsTransformer {

    public MastodonQueryUser getUserDetails(List<UserPermission> userPermissions) {
        return MastodonQueryUser.builder()
                .username(userPermissions.get(0).getUsername())
                .permissions(userPermissions.stream().collect(Collectors.toMap(
                        UserPermission::getDocumentId,
                        permission -> PermissionType.valueOf(permission.getPermissionType())
                )))
                .build();
    }

}
