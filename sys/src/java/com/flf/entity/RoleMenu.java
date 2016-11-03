package com.flf.entity;

public class RoleMenu {

	private int id;
	private int roleid;
	private int menuid;
	private int permissiontype;//权限类型
	private String remark;//备注
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public int getMenuid() {
		return menuid;
	}
	public void setMenuid(int menuid) {
		this.menuid = menuid;
	}
	public int getPermissiontype() {
		return permissiontype;
	}
	public void setPermissiontype(int permissiontype) {
		this.permissiontype = permissiontype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
