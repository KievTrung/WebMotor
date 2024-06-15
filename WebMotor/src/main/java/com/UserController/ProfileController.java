package com.UserController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Entity.Account;
import com.Enums.AFlag;
import com.Utils.UserUtils;

@Controller
public class ProfileController {
	@Autowired
	UserUtils userUtils;
	
	@RequestMapping("profile")
	public String getProfile(ModelMap model){
		model.addAttribute("accountChange", new Account());
		return "Login/profile";
	}
	
	@RequestMapping(value="profile", method=RequestMethod.POST)
	public String saveProfile(ModelMap model, 
								@Valid @ModelAttribute("accountChange") Account user, 
								@RequestParam("old")String oldPass,
								@RequestParam("new")String newPass,
								@RequestParam("renew")String renewPass,
								HttpSession s,
								BindingResult errors) {
		
		Account account = (Account)s.getAttribute("account");
		
		int changeFlag = 0;
		boolean errorFlag = false;
		
		//save current input to check
		String onLoad = "";
		String login = user.getLoginName().trim(), 
				email = user.getEmail().trim(), 
				diaChi = user.getDiaChi().trim();
		Integer phone = user.getPhone();
		
		//set to blank for next input
		user.setLoginName("");
		user.setEmail("");
		user.setDiaChi("");
		user.setPhone(null);
		
		//loginName
		if(!login.equals("")) {
			//check new login for duplicate
			if(userUtils.checkLogin(login) == null) {
				//if no dup then change value
				try {
					userUtils.changeLogin(account.getId(), login);
				} catch (Exception e) {
					//if for some reason that it fail to change then this happen
					errors.rejectValue("loginName", "1", e.getMessage());
					onLoad += "changeLogin();";
					errorFlag = true;
				}
			}
			else {
				errors.rejectValue("loginName", "2", "Invalid login");
				onLoad += "changeLogin();";
				errorFlag = true;
			}
		}
		//password
		if(!oldPass.trim().equals("") && !newPass.trim().equals("") && !renewPass.trim().equals("")) {
			changeFlag |= userUtils.changeAccount(AFlag.PASSWORD);			
			if(!account.getPassword().equals(oldPass)) {
				errors.rejectValue("password", "3", "Invalid old password");
				errorFlag = true;				
			}
			if(!newPass.equals(renewPass)) {
				errors.rejectValue("password", "4", "password not match");
				errorFlag = true;
			}	
			if(errorFlag)
				onLoad += "changePassword();";
		}

		//diaChi
		if(!diaChi.equals("")) {
			changeFlag |=  userUtils.changeAccount(AFlag.ADDRESS);
		}
		//email
		if(!email.equals("")) {
			changeFlag |= userUtils.changeAccount(AFlag.EMAIL);
			if(userUtils.checkEmail(email) != null) {
				errors.rejectValue("email", "5", "Email existed");
				errorFlag = true;
				onLoad += "changeEmail();";
			}
		}
		//phone
		if(phone != null && !errors.hasErrors()) {
			changeFlag |= userUtils.changeAccount(AFlag.PHONE);
			if(userUtils.checkPhone(phone) != null) {
				errors.rejectValue("phone", "6", "Phone existed");
				errorFlag = true;
				onLoad += "changePhone();";
			}
		}
		//if there is new valid change then insert user to db
		String loginName = (login.equals("")) ? account.getLoginName() : login;
		if(changeFlag != 0 && !errorFlag) 
		{
			userUtils.setUpTaiKhoan(changeFlag, 
										loginName, 
										newPass, 
										(phone == null) ? 0 : phone, 
										email, 
										diaChi, 
										false, 
										false);
		}
		
		//reload account
		s.setAttribute("account", userUtils.getUser(loginName));	
		
		model.addAttribute("onLoad", onLoad);
		return "Login/profile";
	}
	
	@RequestMapping("logout")
	public String logOut(HttpSession s) {
		s.invalidate();
		return "redirect:/index";
	}
}
