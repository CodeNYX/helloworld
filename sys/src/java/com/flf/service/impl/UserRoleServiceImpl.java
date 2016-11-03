package com.flf.service.impl;

import java.util.List;
import com.flf.entity.UserRole;
import com.flf.mapper.UserRoleMapper;
import com.flf.service.UserRoleService;

public class UserRoleServiceImpl implements UserRoleService{

	private UserRoleMapper userRoleMapper;

	public UserRoleMapper getUserRoleMapper() {
		return userRoleMapper;
	}

	public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
	}

	public List<UserRole> listAllRolesByUserId(int userid) {
		userRoleMapper.listAllRolesByUserId(userid);
		return userRoleMapper.listAllRolesByUserId(userid);
		
	}

	public void insert(UserRole userRole) {
		// TODO Auto-generated method stub
		userRoleMapper.insertUserRole(userRole);
	}

	public void deleteByUserId(int userId) {
		// TODO Auto-generated method stub
		userRoleMapper.deleteUserRoleByUserId(userId);
	}
	


	
}
