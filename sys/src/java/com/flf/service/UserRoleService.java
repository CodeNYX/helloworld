package com.flf.service;

import java.util.List;

import com.flf.entity.UserRole;

public interface UserRoleService {

	List<UserRole>  listAllRolesByUserId(int userid);//根据用户查找出含有的角色
	
	void insert(UserRole userRole);//插入新的对象
	
	void deleteByUserId(int userId);//删除UserRole根据userid
}
