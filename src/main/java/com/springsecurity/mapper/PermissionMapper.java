package com.springsecurity.mapper;

import com.springsecurity.entity.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper {
    // 查询苏所有权限
    @Select(" select * from sys_permission ")
    List<Permission> findAllPermission();

}
