package com.microsorvices.demo.elastic.query.service.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "user_permissions")
public class UserPermission {

    @NotNull
    @Id
    @Column
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String documentId;

    @NotNull
    private String permissionType;
}
