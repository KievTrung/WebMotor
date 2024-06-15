package com.UserController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Auxiliaries.User;
import com.Enums.AFlag;
import com.Utils.UserUtils;

@Controller
public class PasswordController {
	@Autowired
	UserUtils userUtils;
	
	@RequestMapping(value="changePassword", method=RequestMethod.POST)
	public String changePassword(ModelMap model, 
								@RequestParam("new") String newPass, 
								@RequestParam("renew") String renewPass,
								@RequestParam("code") String code, 
								HttpSession s) {
		int systemCode = (int)s.getAttribute("code"); 
		long expiredTime = (long)s.getAttribute("expiredTime");
		long currentTime = System.currentTimeMillis();
		int userCode;

		try {
			userCode = Integer.parseInt(code);
		}catch(NumberFormatException ex) {
			model.addAttribute("msg", "Invalid code");
			return "Login/password";
		}
		
		if(systemCode != userCode) {
			model.addAttribute("msg", "Invalid code");
			return "Login/password";
		}
		
		if(expiredTime < currentTime) {
			s.invalidate();
			model.addAttribute("msg", "Code has expired, send again");
			return "Login/email";
		}
		
		if(newPass.equals("") || !renewPass.equals(newPass)) {
			model.addAttribute("msg", "Invalid password");
			return "Login/password";
		}
			
		int result = userUtils.setUpTaiKhoan(userUtils.changeAccount(AFlag.PASSWORD), 
																	(String)s.getAttribute("login"), 
																	newPass, 0, "", "", false, false);		
		
		s.invalidate();
		
		if(result == 0) {
			model.addAttribute("msg", "Something wrong");
			return "Login/password";
		}
		
		model.addAttribute("msgLogin", "Change password successfully");
		model.addAttribute("user", new User());
		model.addAttribute("onLoad", "login()");
		return "Login/login";
	}
}
