package com.UserController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Utils.*;
import com.Auxiliaries.*;
import com.Entity.*;

@Controller
public class LoginController {
	@Autowired
	UserUtils userUtils;		
	
	@RequestMapping("login")
	public String login(ModelMap model) {
		model.addAttribute("user", new User());
		model.addAttribute("onLoad", "login()");
		return "Login/login";
	}	
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	public String login(ModelMap model, @Valid @ModelAttribute("user")User user, BindingResult errors, HttpServletRequest request){
		if(errors.hasErrors()) {
			return "Login/login"; 
		}
		
		Account account = userUtils.getUser(user.getUsername(), user.getPassword()); 

		if(account == null) {
			model.addAttribute("msgLogin", "Login nonexisted or Invalid username or password");
			model.addAttribute("onLoad", "login()");
			return "Login/login";
		}
		
		if(!account.isActive()) {
			model.addAttribute("msgLogin", "This account has been deactivate, please contact admin");
			model.addAttribute("onLoad", "login()");
			return "Login/login";
		}
		
		HttpSession s = request.getSession();
		s.setAttribute("account", account);

		if(account.isAdmin()) 
			return "redirect:/admin/index";
		return "redirect:/index";
	}
	
	@RequestMapping("register")
	public String register(@Valid @ModelAttribute("user")User user, BindingResult result, ModelMap model, HttpServletRequest request) {
		
		if(userUtils.getUser(user.getUsername()) == null) 
		{
			if(userUtils.checkEmail(user.getEmail()) == null)
			{				
				if(userUtils.setUpTaiKhoan(
											userUtils.changeAccount(), 
											user.getUsername(), 
											user.getPassword(), 
											0, 
											user.getEmail(),
											"", false, false) == 0) 
				{
					model.addAttribute("msgRegister", "Registered fail");
					model.addAttribute("onLoad", "register()");
					return "Login/login";
				}
				else 
				{				
					HttpSession s = request.getSession();
					s.setAttribute("account", userUtils.getUser(user.getUsername()));
				}
			}
			else
			{
				model.addAttribute("msgRegister", "Email existed");
				model.addAttribute("onLoad", "register()");
				return "Login/login";
			}
		}
		else
		{
			model.addAttribute("msgRegister", "Login name existed");
			model.addAttribute("onLoad", "register()");
			return "Login/login";
		}
		
		return "redirect:/index";
	}

}