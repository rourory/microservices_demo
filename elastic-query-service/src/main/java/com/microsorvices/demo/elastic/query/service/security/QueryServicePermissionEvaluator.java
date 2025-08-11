package com.microsorvices.demo.elastic.query.service.security;

import com.microsorvices.demo.elastic.query.service.common.body.ElasticQueryServiceResponseModel;
import com.microsorvices.demo.elastic.query.service.common.body.ElasticServiceSearchRequestBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class QueryServicePermissionEvaluator implements PermissionEvaluator {

    private static final String SUPERUSER = "APP_SUPERUSER_ROLE";

    private final HttpServletRequest httpServletRequest;

    public QueryServicePermissionEvaluator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        if (isSuperuser()) {
            return true;
        }
        if (targetDomainObject instanceof ElasticServiceSearchRequestBody) {
            return preAuthorize(authentication, ((ElasticServiceSearchRequestBody) targetDomainObject).getId(), permission);
        } else if (targetDomainObject instanceof ResponseEntity || targetDomainObject == null) {
            if (targetDomainObject == null) {
                return true;
            }
            List<ElasticQueryServiceResponseModel> responseBody =
                    ((ResponseEntity<List<ElasticQueryServiceResponseModel>>) targetDomainObject).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, responseBody, permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if (isSuperuser()) {
            return true;
        }
        if (targetId == null) {
            return false;
        }
        var has = preAuthorize(authentication, (String) targetId, permission);
        return has;
    }

    private boolean postAuthorize(Authentication authentication, List<ElasticQueryServiceResponseModel> responseBody, Object permission) {
        MastodonQueryUser user = (MastodonQueryUser) authentication.getPrincipal();
        for (ElasticQueryServiceResponseModel responseModel : responseBody) {
            var permissionType = user.getPermissions().get(responseModel.getId());
            if(permissionType == null) return false;
            if (!hasPermission(permission, permissionType)) {
                return false;
            }
        }
        return true;
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        MastodonQueryUser user = (MastodonQueryUser) authentication.getPrincipal();
        PermissionType permissionType = user.getPermissions().get(id);
        if(permissionType == null) return false;
        var has = hasPermission(permission, permissionType);
        return has;
    }

    private boolean hasPermission(Object permission, PermissionType permissionType) {
        var has = permission != null && permission.equals(permissionType.getType());
        return has;
    }

    private boolean isSuperuser() {
        return httpServletRequest.isUserInRole(SUPERUSER);
    }

}
