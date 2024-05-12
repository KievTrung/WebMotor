package com.Interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.Entity.Account;
import com.Utils.UserUtils;

public class ItemsTrackingInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	UserUtils util;
	
	@Override
	 public boolean preHandle(HttpServletRequest request, 
			 					HttpServletResponse response, 
			 					Object handler) throws Exception{
		HttpSession s = request.getSession();
		Account account = (Account)s.getAttribute("account");
		if(account != null) {
			s.setAttribute("items", util.countCartItems(account.getId()));
		}
		else
			s.setAttribute("items", 0);
		return true;
	}
}
