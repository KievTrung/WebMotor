package com.Controller;


import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Utils.*;
import com.Beans.*;
import com.Entity.*;
import com.Enums.AFlag;

@Controller
public class LoginController {
	@Autowired
	JavaMailSender mailer;

	@Autowired
	UserUtils userUtils;
	
	public int changeAccount(AFlag...flags) {
		int flagCombo = 0;
		for(AFlag flag : flags) {
			flagCombo |= flag.getValue();
		}
		return flagCombo ;
	}
	
	@RequestMapping(value= {"/", "index"})
	public String index(ModelMap model) throws Exception{
		model.addAttribute("categoryList", userUtils.getCategoryPicture());
		model.addAttribute("featureList", userUtils.getFeatureProduct());
		model.addAttribute("specialProduct", userUtils.getSpecialProduct());
		model.addAttribute("indexProduct", userUtils.getIndexProduct());
		return "index";
	}		
	
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
			
		int result = userUtils.setUpTaiKhoan(changeAccount(AFlag.PASSWORD), (String)s.getAttribute("login"), newPass, 0, "", "", false, false);		
		
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
	
	@RequestMapping("forgetPassword")
	public String forgetPassword(HttpSession s, ModelMap model) {
		Account account = (Account)s.getAttribute("account");
		if(account != null)
			model.addAttribute("email", account.getEmail()); 
		return "Login/email";
	}
	
	@RequestMapping(value="sendEmail", method=RequestMethod.POST)
	public String sendEmail(@RequestParam("email")String email, ModelMap model, HttpServletRequest request){
		//check if email is valid 
		
		String login = userUtils.checkEmail(email);
		if(login == null) {
			model.addAttribute("msg", "Email không tồn tại");
			
			return "Login/email";
		}
				
		//send email to user to change password
		try {
			MimeMessage mail = mailer.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mail);
			helper.setFrom(userUtils.getEmail());
			helper.setTo(email);
			helper.setReplyTo(userUtils.getEmail());
			helper.setSubject("Mã bảo mật cho tài khoản " + login);
			
			//generate random number and send it to user via email
			int max = 1000;
			int random = (new Random()).nextInt(max);
			helper.setText(String.valueOf(random));
			
			//put authenication code and loginName and set expired time for the code in the session
			HttpSession s = request.getSession();
			s.setAttribute("code", random);
			s.setAttribute("login", login);
			s.setAttribute("expiredTime", System.currentTimeMillis() + 30000);
			
			mailer.send(mail);
		}catch(Exception ex){
			model.addAttribute("msg", "Đã có lỗi xảy ra");
			ex.printStackTrace();
			return "Login/email";
		}
		
		model.addAttribute("msg", "Code has been sent, check email, 30s until expired");
		return "Login/password";
	}
	
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
		
		Account account = userUtils.checkUser(user.getUsername(), user.getPassword()); 
		
		if(account == null) {
			model.addAttribute("msgLogin", "Login nonexisted or Invalid username or password");
			model.addAttribute("onLoad", "login()");
			return "Login/login";
		}
				
		HttpSession s = request.getSession();
		s.setAttribute("account", account);
		
		return "redirect:/index";
	}
	
	@RequestMapping("register")
	public String register(@Valid @ModelAttribute("user")User user, BindingResult result, ModelMap model, HttpServletRequest request) {
		
		if(userUtils.getUser(user.getUsername()) == null) 
		{
			if(userUtils.checkEmail(user.getEmail()) == null)
			{				
				if(userUtils.setUpTaiKhoan(
											changeAccount(), 
											user.getUsername(), 
											user.getPassword(), 
											0, 
											user.getEmail(),
											"", 
											false, 
											false) == 0) 
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
								HttpServletRequest request,
								BindingResult errors) {
		
		HttpSession s = request.getSession();
		Account account = (Account)s.getAttribute("account");
		
		int changeFlag = 0;
		boolean errorFlag = false;
		
		String onLoad = "";
		String login = user.getLoginName().trim(), email = user.getEmail().trim(), diaChi = user.getDiaChi().trim();
		int phone = user.getPhone();
		
		user.setLoginName("");
		user.setEmail("");
		user.setDiaChi("");
		user.setPhone(0);
		
		//loginName
		if(!login.equals("")) {			
			Integer id = userUtils.checkLogin(login);
			if(id == null) {
				if(userUtils.changeLogin(account.getId(), login) == 0) {
					errors.rejectValue("loginNameError", "1", "Changed login fail");
					onLoad += "changeLogin();";
				}
			}
			else {
				errors.rejectValue("loginName", "2", "Invalid login");
				onLoad += "changeLogin();";
			}
		}
		//password
		if(!oldPass.trim().equals("") && !newPass.trim().equals("") && !renewPass.trim().equals("")) {
			changeFlag |= changeAccount(AFlag.PASSWORD);			
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
			changeFlag |=  changeAccount(AFlag.ADDRESS);
		}
		//email
		if(!email.equals("")) {
			changeFlag |= changeAccount(AFlag.EMAIL);
			if(userUtils.checkEmail(email) != null) {
				errors.rejectValue("email", "5", "Email existed");
				errorFlag = true;
				onLoad += "changeEmail();";
			}
		}
		//phone
		if(phone != 0 && !errors.hasErrors()) {
			changeFlag |= changeAccount(AFlag.PHONE);
			if(userUtils.checkPhone(phone) != null) {
				errors.rejectValue("phone", "6", "Phone existed");
				errorFlag = true;
				onLoad += "changePhone();";
			}
		}
		//check condition and insert user to db
		String loginName = (login.equals("")) ? account.getLoginName() : login;
		if(changeFlag != 0 && !errorFlag) 
		{
			if(userUtils.setUpTaiKhoan(
					changeFlag, 
					loginName, 
					newPass, 
					phone, 
					email, 
					diaChi, 
					false, 
					false) == 0) 
			{
				System.out.println("somthing wrong");
			}
		}
		//reload account
		System.out.println(loginName);
		s.setAttribute("account", userUtils.getUser(loginName));	
		
		
		model.addAttribute("onLoad", onLoad);
		return "Login/profile";
	}
	
	@RequestMapping("logout")
	public String logOut(HttpSession s) {
		s.invalidate();
		return "index";
	}

}