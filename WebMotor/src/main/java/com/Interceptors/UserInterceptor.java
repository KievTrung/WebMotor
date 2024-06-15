package com.Interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInterceptor extends HandlerInterceptorAdapter{
	@Override
	 public boolean preHandle(HttpServletRequest request, 
			 					HttpServletResponse response, 
			 					Object handler) throws Exception{
		HttpSession s = request.getSession();
		if(s.getAttribute("account") == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
		return true;
	}
	
}
