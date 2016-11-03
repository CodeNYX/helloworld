package com.flf.controller;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.flf.entity.Role;
import com.flf.entity.User;
import com.flf.service.RoleService;
import com.flf.service.UserService;
import com.flf.view.UserExcelView;

@Controller
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	/**
	 * 显示用户列表
	 * @param user
	 * @return
	 */
	@RequestMapping
	public ModelAndView list(User user){
		//使用shiro对当前操作进行授权认证
		Subject subject = SecurityUtils.getSubject();
		ModelAndView mv = new ModelAndView();
		if(subject.hasRole("admin")){
			//List<User> userList = userService.listAllUser(page);
			List<User> userList = userService.listPageUser(user);
			List<Role> roleList = roleService.listAllRoles();
			mv.setViewName("users");
			mv.addObject("userList", userList);
			mv.addObject("roleList", roleList);
			mv.addObject("user", user);
		}else{
			System.out.println("权限检测没有通过");
			mv.setViewName("index");
		}
		return mv;
	}
	
	
	/**
	 * 请求新增用户页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String toAdd(Model model){
		List<Role> roleList = roleService.listAllRoles();
		model.addAttribute("roleList", roleList);
		return "user_info";
	}
	
	/**
	 * 保存用户信息
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public ModelAndView saveUser(User user){
		ModelAndView mv = new ModelAndView();
		if(user.getUserId()==null || user.getUserId().intValue()==0){
			if(userService.insertUser(user)==false){
				mv.addObject("msg","failed");
			}else{
				mv.addObject("msg","success");
			}
		}else{
			userService.updateUserBaseInfo(user);
		}
		mv.setViewName("save_result");
		return mv;
	}
	
	
	/**
	 * 请求编辑用户页面
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/edit")
	public ModelAndView toEdit(@RequestParam int userId){
		ModelAndView mv = new ModelAndView();
		User user = userService.getUserById(userId);
		List<Role> roleList = roleService.listAllRoles();
		mv.addObject("user", user);
		mv.addObject("roleList", roleList);
		mv.setViewName("user_info");
		return mv;
	}
	
	/**
	 * 删除某个用户
	 * @param userId
	 * @param out
	 */
	@RequestMapping(value="/delete")
	public void deleteUser(@RequestParam int userId,PrintWriter out){
		userService.deleteUser(userId);
		out.write("success");
		out.close();
	}
	
	
	/**
	 * 导出用户信息到excel
	 * @return
	 */
	@RequestMapping(value="/excel")
	public ModelAndView export2Excel(){
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("用户名");
		titles.add("名称");
		titles.add("角色");
		titles.add("最近登录");
		dataMap.put("titles", titles);
		List<User> userList = userService.listAllUser();
		dataMap.put("userList", userList);
		UserExcelView erv = new UserExcelView();
		ModelAndView mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
}












