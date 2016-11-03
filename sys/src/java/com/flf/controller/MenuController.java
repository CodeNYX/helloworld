package com.flf.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.flf.entity.Menu;
import com.flf.service.MenuService;

@Controller
@RequestMapping(value="/menu")
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	/**
	 * 显示菜单列表
	 * @param model
	 * @return
	 */
	@RequestMapping
	public String list(Model model){
		//使用shiro对当前操作进行授权认证
		String pageurl = null;
		Subject subject = SecurityUtils.getSubject();
		if(subject.hasRole("admin")){
			List<Menu> menuList = menuService.listAllParentMenu();
			model.addAttribute("menuList", menuList);
			pageurl = "menus";
		}else{
			pageurl = "index";
			System.out.println("没有进入菜单权限");
		}
		return pageurl;
		
	}
	
	/**
	 * 请求新增菜单页面(添加父级菜单)
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add")
	public String toAdd(Model model){
		List<Menu> menuList = menuService.listAllParentMenu();
		model.addAttribute("menuList", menuList);
		return "menus_info";
	}
	
	/**
	 * 请求编辑菜单页面
	 * @param menuId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String toEdit(@RequestParam Integer menuId,Model model){
		Menu menu = menuService.getMenuById(menuId);
		model.addAttribute("menu", menu);
		if(menu.getParentId()!=null && menu.getParentId().intValue()>0){
			List<Menu> menuList = menuService.listAllParentMenu();
			model.addAttribute("menuList", menuList);
		}
		return "menus_info";
	}
	
	/**
	 * 保存菜单信息
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/save")
	public String save(Menu menu,Model model){
		menuService.saveMenu(menu);
		model.addAttribute("msg", "success");
		return "save_result";
	}
	
	/**
	 * 获取当前菜单的所有子菜单
	 * @param menuId
	 * @param response
	 */
	@RequestMapping(value="/sub")
	public void getSub(@RequestParam Integer menuId,HttpServletResponse response){
		List<Menu> subMenu = menuService.listSubMenuByParentId(menuId);
		JSONArray arr = JSONArray.fromObject(subMenu);
		PrintWriter out;
		try {
			response.setCharacterEncoding("utf-8");
			out = response.getWriter();
			String json = arr.toString();
			out.write(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除菜单
	 * @param menuId
	 * @param out
	 */
	@RequestMapping(value="/del")
	public void delete(@RequestParam Integer menuId,PrintWriter out){
		menuService.deleteMenuById(menuId);
		out.write("success");
		out.flush();
		out.close();
	}
}
