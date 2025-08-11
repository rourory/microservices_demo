package com.microsorvices.demo.elastic.query.service.service;

import com.microsorvices.demo.elastic.query.service.data.entity.UserPermission;

import java.util.List;
import java.util.Optional;

public interface QueryUserService {

    Optional<List<UserPermission>> getUserPermissionsByUsername(String username);

}
