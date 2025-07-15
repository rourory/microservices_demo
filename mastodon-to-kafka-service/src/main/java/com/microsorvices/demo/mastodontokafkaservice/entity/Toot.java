package com.microsorvices.demo.mastodontokafkaservice.entity;

import lombok.Data;

@Data
public class Toot {
    private String id;
    private String content;
    private String username;
    private String createdAt;
}