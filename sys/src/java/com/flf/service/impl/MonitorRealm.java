package com.flf.service.impl;

import java.util.HashSet;
import java.util.Set;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.flf.entity.Role;
import com.flf.entity.User;
import com.flf.service.MenuService;
import com.flf.service.RoleService;
import com.flf.service.UserService;

public class MonitorRealm extends AuthorizingRealm {
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	MenuService menuService;

	public MonitorRealm() {
		super();

	}

	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		/* 这里编写授权代码(基于用户角色的授权) */
		String username = (String)principals.getPrimaryPrincipal();
		User user = userService.getUserByUserName(username);
		Role role = roleService.getRoleById(user.getRoleId());
		Set<String> roleNames = new HashSet<String>();
		roleNames.add(role.getRoleName());
		Set<String> permissions = new HashSet<String>();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
	    info.setStringPermissions(permissions);
		return info;

	}

	//认证,登录验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		System.out.println("认证");
		/* 这里编写认证代码 */
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String password = String.valueOf(token.getPassword());
		User user = userService.getUserByNameAndPwd(token.getUsername(), password);
		if(user==null){
			throw new AccountException("帐号或密码不正确！");
		}
		return new SimpleAuthenticationInfo(user.getLoginname(),
				user.getPassword(), getName());

	}

	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

}
