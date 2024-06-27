package com.UserController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Utils.UserUtils;

@Controller
public class UserIndexController {
	@Autowired
	ServletContext context;
	
	@Autowired
	UserUtils userUtils;
	
	@RequestMapping(value= {"/", "index"})
	public String index(ModelMap model, HttpSession s) throws Exception{
		model.addAttribute("categoryList", userUtils.getIndexCategory());
		model.addAttribute("featureList", userUtils.getTop8BestSellerProduct());
		model.addAttribute("indexProduct", userUtils.getIndexProduct((String)context.getAttribute("frontPage"), context));
		model.addAttribute("specialProduct", userUtils.getSpecialProduct((String)context.getAttribute("specialOffer"), context));
		return "User/index";
	}
}
