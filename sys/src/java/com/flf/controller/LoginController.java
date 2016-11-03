package com.flf.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.flf.entity.Menu;
import com.flf.entity.Role;
import com.flf.entity.RoleMenu;
import com.flf.entity.User;
import com.flf.service.MenuService;
import com.flf.service.RoleMenuService;
import com.flf.service.UserService;
import com.flf.util.Const;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuService roleMenuService;
	
	/**
	 * 访问登录页
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String loginGet(){
		return "login";
	}
	
	/**
	 * 请求登录，验证用户
	 * @param session
	 * @param loginname
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView loginPost(HttpSession session,String loginname,String password,String code,HttpServletRequest request){
		String errInfo = "用户名或密码错误";
		ModelAndView mv = new ModelAndView();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(loginname, password);
		token.setRememberMe(true);
		try{
			currentUser.login(token);
			//判断认真是否通过,返回true是通过
			if(currentUser.isAuthenticated()){
				User user = userService.getUserByNameAndPwd(loginname, password);
				session.setAttribute(Const.SESSION_USER, user);
				mv.setViewName("redirect:index.html");
			}else{
				mv.addObject("errInfo", errInfo);
				mv.addObject("loginname",loginname);
				mv.addObject("password",password);
				mv.setViewName("login");
			}
		}catch(AuthenticationException e){
			e.printStackTrace();
			mv.addObject("errInfo", errInfo);
			mv.addObject("loginname",loginname);
			mv.addObject("password",password);
			mv.setViewName("login");
		}
		
		return mv;
	}
	
	/**
	 * 访问系统首页
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(HttpSession session,Model model){
		User user = (User)session.getAttribute(Const.SESSION_USER);
		user = userService.getUserAndRoleById(user.getUserId());
		Role role = user.getRole();
		String roleRights = role!=null ? role.getRights() : "";
		List<RoleMenu> roleMenuList = roleMenuService.getRoleMenuByRoleId(role.getRoleId());
		String rights = "";
		List<Menu> roleRightsList = new ArrayList<Menu>();//当前角色具有的菜单集合
		List<Menu> menuList = menuService.listAllMenu();//数据库中所有的菜单
		Menu menu = null;
		RoleMenu roleMenu = null;
		for(RoleMenu roleMemu : roleMenuList){
			menu = menuService.getMenuById(roleMemu.getMenuid());
			if(menu!=null){
				roleRightsList.add(menu);
				//rights += roleMemu.getMenuid();
			}
		}
		
		Iterator<RoleMenu> it = roleMenuList.iterator();
		while(it.hasNext()){
			roleMenu = it.next();
			rights += roleMenu.getMenuid();
			if(it.hasNext()){
				rights+=",";
			}
		}
		System.out.println("角色权限为: "+rights);
		
		if(!roleRightsList.isEmpty()){
			for(Menu oneOfAll : menuList){
				for(Menu roleOfMenu : roleRightsList){
					if(oneOfAll.getMenuId()==roleOfMenu.getMenuId()){
						oneOfAll.setHasMenu(true);
					}
					if(oneOfAll.isHasMenu()){
						List<Menu> subMenuList = oneOfAll.getSubMenu();
						for(Menu sub : subMenuList){
							for(Menu mu : roleRightsList){
								if(sub.getMenuId() == mu.getMenuId()){
									sub.setHasMenu(true);
								}
							}
						}
					}
				}
			}	
		}
		
		session.setAttribute(Const.SESSION_ROLE_RIGHTS, rights); //将角色权限存入session
		
		/*if(Tools.notEmpty(roleRights)){
			for(Menu menu : menuList){
				//遍历menuList中的权限是否是用户含有的权限,是,则为true,
				menu.setHasMenu(RightsHelper.testRights(roleRights, menu.getMenuId()));
				//如果判断菜单被包含在用户菜单中,则获取该菜单包含的下级子菜单
				if(menu.isHasMenu()){
					List<Menu> subMenuList = menu.getSubMenu();
					//循环遍历判断子菜单是否是用户含有的菜单,是,则为true
					for(Menu sub : subMenuList){
						sub.setHasMenu(RightsHelper.testRights(roleRights, sub.getMenuId()));
					}
				}
			}
		}*/
		model.addAttribute("user", user);
		model.addAttribute("menuList", menuList);
		return "index";
	}
	
	/**
	 * 进入首页后的默认页面
	 * @return
	 */
	@RequestMapping(value="/default")
	public String defaultPage(){
		return "default";
	}
	
	/**
	 * 用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/logout")
	public String logout(HttpSession session){
		SecurityUtils.getSubject().logout();
		return "login";
	}
}
