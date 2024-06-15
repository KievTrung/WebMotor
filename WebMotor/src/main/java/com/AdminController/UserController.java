package com.AdminController;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Enums.Role;
import com.Enums.ActiveState;
import com.Utils.AdminUtils;
import com.Utils.UserUtils;

@Controller
@RequestMapping("admin")
public class UserController {
	@Autowired
	AdminUtils adminUtils;
	
	@Autowired
	UserUtils userUtils;
	
	private int defaultPageIndex = 0;
	private Role defaultRoleType = Role.BOTH;
	private ActiveState defaultActiveState = ActiveState.YESNO;
	private String defaultKeyword = null;
	
	@RequestMapping("user")
	public String getUser(ModelMap model, HttpSession s) {
		model.addAttribute("users", adminUtils.getUserList(defaultPageIndex, defaultRoleType, defaultActiveState, defaultKeyword));
		
		s.removeAttribute("numberOfUserPage");
		s.removeAttribute("numberOfUser");
		s.removeAttribute("role");
		s.removeAttribute("activeState");
		s.removeAttribute("username");
		
		long numberOfUser = adminUtils.getNumberOfUsers(defaultRoleType, defaultActiveState, defaultKeyword);
		s.setAttribute("numberOfUser", numberOfUser);
		s.setAttribute("numberOfUserPage", userUtils.getNumberOfItemPages(numberOfUser, adminUtils.getItemsPerPage()));
		s.setAttribute("userPageIndex", defaultPageIndex);
		s.setAttribute("role", defaultRoleType);
		s.setAttribute("activeState", defaultActiveState);
		s.setAttribute("username", defaultKeyword);

		return "Admin/UserManagement/user";
	}
	
	@RequestMapping("userPageIndex")
	public String pageIndex(ModelMap model, HttpSession s, @RequestParam("i")int userPageIndex) {
		
		s.setAttribute("userPageIndex", userPageIndex);
		
		Role role = (Role)s.getAttribute("role");		
		switch(role) {
		case ADMIN:
			model.addAttribute("admin", "selected");
			break;
		case USER:
			model.addAttribute("user", "selected");
			break;
		case BOTH:
		default:
			model.addAttribute("all", "selected");
		}
		
		ActiveState activeState = (ActiveState)s.getAttribute("activeState");
		switch(activeState) {
		case YES:
			model.addAttribute("active", "selected");
			break;
		case NO:
			model.addAttribute("notActive", "selected");
			break;
		case YESNO:
		default:
			model.addAttribute("both", "selected");
		}
		
		model.addAttribute("users", adminUtils.getUserList(userPageIndex, role, activeState, (String)s.getAttribute("username")));
		return "Admin/UserManagement/user";
	}
	
	@RequestMapping(value="userFilter", method=RequestMethod.POST)
	public String searchUser(ModelMap model, HttpSession s,
								@RequestParam("username") String username, 
								@RequestParam("role")Role role,
								@RequestParam("activeState")ActiveState activeState) {

		s.setAttribute("role", role);
		s.setAttribute("activeState", activeState);
		s.setAttribute("username", username);
		
		long numberOfUser = adminUtils.getNumberOfUsers(role, activeState, username);
		s.setAttribute("numberOfUser", numberOfUser);
		s.setAttribute("numberOfUserPage", userUtils.getNumberOfItemPages(numberOfUser, adminUtils.getItemsPerPage()));

		return "redirect:/admin/userPageIndex?i=0";
	}
}
