package com.flf.service.impl;

import java.util.List;

import com.flf.entity.RoleMenu;
import com.flf.mapper.RoleMenuMapper;
import com.flf.service.RoleMenuService;

public class RoleMenuServiceImpl implements RoleMenuService{

	private RoleMenuMapper roleMenuMapper;

	public RoleMenuMapper getRoleMenuMapper() {
		return roleMenuMapper;
	}

	public void setRoleMenuMapper(RoleMenuMapper roleMenuMapper) {
		this.roleMenuMapper = roleMenuMapper;
	}

	public List<RoleMenu> selectAllRoleMenu() {
		// TODO Auto-generated method stub
		return roleMenuMapper.selectAllRoleMenu();
	}

	public List<RoleMenu> getRoleMenuByRoleId(int roleId) {
		// TODO Auto-generated method stub
		return roleMenuMapper.getRoleMenuByRoleId(roleId);
	}

	public void updateRoleRights(RoleMenu roleMenu) {
		// TODO Auto-generated method stub
		roleMenuMapper.update(roleMenu.getId());
	}

	public RoleMenu getBeanByMenuIdAndRoleId(int roleId, int menuId) {
		// TODO Auto-generated method stub
		RoleMenu rolemenu = roleMenuMapper.getBeanByRoleIdAndMenuId(roleId,menuId);
		return rolemenu;
	}

	public void insertRoleMenu(RoleMenu roleMenu) {
		// TODO Auto-generated method stub
		roleMenuMapper.insertOne(roleMenu);
	}

	public void delete(RoleMenu rmData) {
		// TODO Auto-generated method stub
		roleMenuMapper.deleteRoleMenu(rmData);
	}

	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		roleMenuMapper.deleteById(id);
	}

	public void deleteByRoleId(int roleId) {
		// TODO Auto-generated method stub
		roleMenuMapper.deleteByRoleId(roleId);
	}

	
}
