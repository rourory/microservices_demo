package com.microsorvices.demo.elastic.query.service.data.repository;

import com.microsorvices.demo.elastic.query.service.data.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

    @Query(nativeQuery = true, value = "SELECT p.user_permission_id as id, u.username, d.document_id, p.permission_type " +
            "FROM public.users AS u " +
            "JOIN public.user_permissions AS p ON u.id = p.user_id " +
            "JOIN public.documents AS d ON p.document_id = d.id " +
            "WHERE u.username = :username")
    Optional<List<UserPermission>> findUserPermissionsByUsername(@Param("username") String username);
}
