package com.UserController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Entity.Account;
import com.Utils.AdminUtils;
import com.Utils.UserUtils;

@Controller
public class CartController {
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	AdminUtils adminUtils;
	
	@RequestMapping("cart")
	public String getCart(ModelMap model, @RequestParam("id")Integer userId, HttpSession s) {

		model.addAttribute("cartItems", userUtils.getCart( userId));
		
		s.setAttribute("vat", adminUtils.getVat());
		return "User/cart";
	}
	
	@RequestMapping("remove")
	public String removeItem(@RequestParam("productId")String productId, HttpSession s) {
		
		int userId = ((Account)s.getAttribute("account")).getId();
		
		userUtils.removeCartItems( productId, userId);
		
		return "redirect:/cart?id=" + userId;
	}
	
	@RequestMapping(value="changeAmount", method=RequestMethod.POST)
	public String changeAmount(ModelMap model, 
							@RequestParam("itemCode")String itemCode,
							@RequestParam("amount")int amount, 
							HttpSession s, 
							RedirectAttributes rd) {
		int userId = ((Account)s.getAttribute("account")).getId();
		
		if(userUtils.changeItemAmountInCart( userId, itemCode, amount) == 0) {
			rd.addFlashAttribute("itemError", itemCode);
			rd.addFlashAttribute("errorMsg", "Not enough quantities to provide");
		}
		
		return "redirect:/cart?id=" + userId;
	}
}
