package com.microsorvices.demo.elastic.query.service.service.impl;

import com.microsorvices.demo.elastic.query.service.data.entity.UserPermission;
import com.microsorvices.demo.elastic.query.service.data.repository.UserPermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QueryUserService implements com.microsorvices.demo.elastic.query.service.service.QueryUserService {

    private final UserPermissionRepository permissionRepository;

    public QueryUserService(UserPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<List<UserPermission>> getUserPermissionsByUsername(String username) {
        log.info("Trying to get permissions of user: {} on Service layer", username);
        return permissionRepository.findUserPermissionsByUsername(username);
    }
}
