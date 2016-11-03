package com.flf.interceptor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.flf.entity.Menu;
import com.flf.entity.User;
import com.flf.util.Const;
import com.flf.util.ServiceHelper;
import com.flf.util.Tools;

public class RightsHandlerInterceptor extends HandlerInterceptorAdapter{

	/**
	 * 对请求的拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		boolean hasRight = false;
		// TODO Auto-generated method stub
		System.out.println("请求路劲拦截权限判断");
		String path = request.getServletPath();
		if(path.matches(Const.NO_INTERCEPTOR_PATH))
			return true;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Const.SESSION_USER);
		Integer menuId = null;
		List<Menu> subList = ServiceHelper.getMenuService().listAllSubMenu();
		for(Menu m : subList){
			String menuUrl = m.getMenuUrl();
			if(Tools.notEmpty(menuUrl)){
				if(path.contains(menuUrl)){
					menuId = m.getMenuId();
					break;
				}else{
					String[] arr = menuUrl.split("\\.");
					String regex = "";
					if(arr.length==2){
						regex = "/?"+arr[0]+"(/.*)?."+arr[1];
						
					}else{
						regex = "/?"+menuUrl+"(/.*)?.html";
					}
					if(path.matches(regex)){
						menuId = m.getMenuId();
						break;
					}
				}
			}
		}
		if(menuId!=null){
			String roleRights = (String) session.getAttribute(Const.SESSION_ROLE_RIGHTS);
			String [] menuids = roleRights.split(",");
			for(String str:menuids){
				if(Integer.parseInt(str)==menuId){
					hasRight = true;
				}
			}
			
			if(hasRight){
				return true;
			}else{
				System.out.println("用户："+user.getLoginname()+"试图访问"+path+"被阻止！");
				ModelAndView mv = new ModelAndView();
				mv.setViewName("no_rights");
				throw new  ModelAndViewDefiningException(mv);
			}
			/*if(RightsHelper.testRights(roleRights, menuId)){
				return true;
			}else{
				System.out.println("用户："+user.getLoginname()+"试图访问"+path+"被阻止！");
				ModelAndView mv = new ModelAndView();
				mv.setViewName("no_rights");
				throw new  ModelAndViewDefiningException(mv);
			}*/
		}
		return super.preHandle(request, response, handler);
	}
	
}
