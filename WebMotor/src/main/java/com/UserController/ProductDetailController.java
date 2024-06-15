package com.UserController;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Auxiliaries.Product;
import com.Utils.UserUtils;

@Controller
public class ProductDetailController {
	@Autowired
	UserUtils userUtils;
	
	@RequestMapping("productDetail")
	public String getProductDetail(ModelMap model, @RequestParam("code")String maXe) {
		
		Product product = userUtils.getProduct( maXe);
		model.addAttribute("product", product);
		model.addAttribute("relateList", userUtils.getRelateProduct( maXe, product.getType()));
		model.addAttribute("productPictures", userUtils.getProductPicture( maXe));
		
		return "Product/productDetail";
	}
	
	@RequestMapping(value="addCart", method=RequestMethod.POST)
	public String addCart(ModelMap model, 
						@RequestParam("id")Integer id, 
						@RequestParam("product")String maXe,
						@RequestParam("amount")int soLuong, 
						HttpSession s,
						RedirectAttributes rd) {

		if(userUtils.themGioHang( id, maXe, soLuong) == 0)
			rd.addFlashAttribute("errorMsg", "Not enough quantities to provide");
		
		return "redirect:/productDetail?code=" + maXe;
	}
}
