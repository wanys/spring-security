package com.springsecurity.entity;

import lombok.Data;

@Data
public class Role {
    private Integer id;
    private String roleName;
    private String roleDesc;
}
