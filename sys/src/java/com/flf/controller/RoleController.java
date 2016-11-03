package com.flf.controller;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.flf.entity.Menu;
import com.flf.entity.Role;
import com.flf.entity.RoleMenu;
import com.flf.service.MenuService;
import com.flf.service.RoleMenuService;
import com.flf.service.RoleService;
import com.flf.util.RightsHelper;
import com.flf.util.Tools;

@Controller
@RequestMapping(value="/role")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuService roleMenuService;
	
	
	/**
	 * 显示角色列表
	 * @param map
	 * @return
	 */
	@RequestMapping
	public String list(Map<String,Object> map){
		//使用shiro对当前操作进行授权认证
		String pageurl = null;
		Subject subject = SecurityUtils.getSubject();
		if(subject.hasRole("admin")){
			List<Role> roleList = roleService.listAllRoles();
			map.put("roleList", roleList);
			pageurl = "roles";
		}else{
			System.out.println("权限检测没有通过");
			pageurl = "index";
		}
		return pageurl;
	}
	
	/**
	 * 保存角色信息
	 * @param out
	 * @param role
	 */
	@RequestMapping(value="/save")
	public void save(PrintWriter out,Role role){
		boolean flag = true;
		if(role.getRoleId()!=null && role.getRoleId().intValue()>0){
			flag = roleService.updateRoleBaseInfo(role);
		}else{
			flag = roleService.insertRole(role);
		}
		if(flag){
			out.write("success");
		}else{
			out.write("failed");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 请求角色授权页面
	 * @param roleId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/auth")
	public String auth(@RequestParam int roleId,Model model){
		List<Menu> menuList = menuService.listAllMenu();
		Role role = roleService.getRoleById(roleId);
		List<RoleMenu> roleMenus = roleMenuService.getRoleMenuByRoleId(role.getRoleId());
		Menu mu = null;
		List<Menu> roleMenuList = new ArrayList<Menu>();
		if(!roleMenus.isEmpty()){
			for(RoleMenu roleMenu : roleMenus){
				mu = menuService.getMenuById(roleMenu.getMenuid());
				if(null != mu){
					roleMenuList.add(mu);
				}
			}
		}
		if(!roleMenuList.isEmpty()){
			for(Menu oneOfAll : menuList){
				for(Menu roleOfMenu : roleMenuList){
					if(oneOfAll.getMenuId()==roleOfMenu.getMenuId()){
						oneOfAll.setHasMenu(true);
					}
					if(oneOfAll.isHasMenu()){
						List<Menu> subMenuList = oneOfAll.getSubMenu();
						for(Menu sub : subMenuList){
							for(Menu men : roleMenuList){
								if(sub.getMenuId() == men.getMenuId()){
									sub.setHasMenu(true);
								}
							}
						}
					}
				}
			}	
		}
		
		JSONArray arr = JSONArray.fromObject(menuList);
		String json = arr.toString();
		json = json.replaceAll("menuId", "id").replaceAll("menuName", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked");
		model.addAttribute("zTreeNodes", json);
		model.addAttribute("roleId", roleId);
		return "authorization";
	}
	
	/**
	 * 保存角色权限
	 * @param roleId
	 * @param menuIds
	 * @param out
	 */
	@RequestMapping(value="/auth/save")
	public void saveAuth(@RequestParam int roleId,@RequestParam String menuIds,PrintWriter out){
		//简单粗暴,删掉所有,添加前台传入的所有数据
		
		String [] menuIdArray = menuIds.split(",");
		RoleMenu roleMenu = null;
		List<RoleMenu> roleMenuJSP = new ArrayList<RoleMenu>();
		for(int i=0;i<=menuIdArray.length-1;i++){
			roleMenu = new RoleMenu();
			roleMenu.setRoleid(roleId);
			roleMenu.setMenuid(Integer.parseInt(menuIdArray[i]));
			roleMenuJSP.add(roleMenu);
		}
		
		roleMenuService.deleteByRoleId(roleId);
		for(RoleMenu rm : roleMenuJSP){
			roleMenuService.insertRoleMenu(rm);
		}
		
		/*roleMenuService.updateRoleRights(roleMenu)
		BigInteger rights = RightsHelper.sumRights(Tools.str2StrArray(menuIds));
		Role role = roleService.getRoleById(roleId);
		role.setRights(rights.toString());
		roleService.updateRoleRights(role);*/
		out.write("success");
		out.close();
	}
}
