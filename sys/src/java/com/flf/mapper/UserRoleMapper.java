package com.flf.mapper;

import java.util.List;


import com.flf.entity.UserRole;

public interface UserRoleMapper {


	List<UserRole> listAllRolesByUserId(int userid);
	void insertUserRole(UserRole userRole);
	void deleteUserRoleByUserId(int userId);
}
