package com.UserController;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Entity.Account;
import com.Utils.UserUtils;

@Controller
public class EmailController {
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	JavaMailSender mailer;
	
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
}
