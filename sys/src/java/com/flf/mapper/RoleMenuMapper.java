package com.flf.mapper;

import java.util.List;

import com.flf.entity.RoleMenu;

public interface RoleMenuMapper {

	List<RoleMenu> selectAllRoleMenu();

	List<RoleMenu> getRoleMenuByRoleId(int roleId);

	void update(int id);

	RoleMenu getBeanByRoleIdAndMenuId(int roleId, int menuId);

	void insertOne(RoleMenu roleMenu);

	void deleteRoleMenu(RoleMenu rmData);

	void deleteById(Integer id);

	void deleteByRoleId(int roleId);
}
