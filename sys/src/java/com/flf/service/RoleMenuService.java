package com.flf.service;

import java.util.List;

import com.flf.entity.Role;
import com.flf.entity.RoleMenu;

public interface RoleMenuService {
	
	List<RoleMenu> selectAllRoleMenu();
	
	List<RoleMenu> getRoleMenuByRoleId(int roleId);
	
	void updateRoleRights(RoleMenu roleMenu);

	RoleMenu getBeanByMenuIdAndRoleId(int roleId, int i);
	
	void insertRoleMenu(RoleMenu roleMenu);

	void delete(RoleMenu rmData);

	void deleteById(Integer id);

	void deleteByRoleId(int roleId);
}
