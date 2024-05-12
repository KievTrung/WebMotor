package com.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Beans.Product;
import com.Entity.Account;
import com.Enums.SortType;
import com.Utils.UserUtils;


@Controller
public class ProductController {
	@Autowired
	UserUtils userUtils;
	
	private int defaultPageIndex = 0;
	String defaultVehicleType = "All";
	SortType defaultSortType = SortType.A2Z;
	String defaultKeyword = "";

	@ModelAttribute("productTypeList")
	public List<String> getItems(){
		return userUtils.getLoaiXe();
	}
	
	@RequestMapping("product")
	public String getDefaultPage(HttpSession s, ModelMap model){
		
		//get a list of defaultPageIndex and put it in jsp 
		model.addAttribute("productList", userUtils.getChiTietXeList(defaultPageIndex, defaultSortType, defaultVehicleType, defaultKeyword));
		model.addAttribute("productType", "All products");
		model.addAttribute("a2z", "selected");
		
		s.removeAttribute("numberOfPage");
		s.removeAttribute("sortType");
		s.removeAttribute("vehicleType");
		s.removeAttribute("keyword");
		s.removeAttribute("numberOfRecords");

		s.setAttribute("numberOfPage", userUtils.getNumberOfPages(s, defaultVehicleType, defaultKeyword));
		s.setAttribute("pageIndex", defaultPageIndex);
		s.setAttribute("sortType", defaultSortType);
		s.setAttribute("vehicleType", defaultVehicleType);
		s.setAttribute("keyword", defaultKeyword);
		
		defaultVehicleType = "All";
		
		return "Product/products";
	}

	
	@RequestMapping("pageIndex")
	public String getPage(ModelMap model, HttpSession s, @RequestParam("i") int pageIndex) {
		
		s.setAttribute("pageIndex", pageIndex);
		
		SortType sType = (SortType)s.getAttribute("sortType");		
		switch(sType) {
		case LOW2HIGH:
			model.addAttribute("l2h", "selected");
			break;
		case A2Z:
			model.addAttribute("a2z", "selected");
			break;
		case Z2A:
			model.addAttribute("z2a", "selected");
			break;
		case HIGH2LOW:
		default:
			model.addAttribute("h2l", "selected");
		}
				
		//get a list of pageIndex and put it in jsp 
		model.addAttribute("productList", userUtils.getChiTietXeList(pageIndex, sType, (String)s.getAttribute("vehicleType"), (String)s.getAttribute("keyword")));
		
		return "Product/products";
	}
	
	@RequestMapping(value="filter", method=RequestMethod.POST)
	public String filterProduct(ModelMap model, 
								@RequestParam("type")String vType,
								@RequestParam("sort")SortType sType,
								@RequestParam("key")String keyword,
								HttpSession s) {
		s.setAttribute("keyword", keyword.trim());
		s.setAttribute("numberOfPage", userUtils.getNumberOfPages(s, vType, (String)s.getAttribute("keyword")));
		
		s.setAttribute("vehicleType", vType);
		s.setAttribute("sortType", sType);
				
		return "redirect:/pageIndex?i=0";
	}
	
	@RequestMapping("category")
	public String getCategory(ModelMap model, @RequestParam("category")String vehicleType) {
		defaultVehicleType = vehicleType;
		return "redirect:/product";
	}
	
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
		
	@RequestMapping("cart")
	public String getCart(ModelMap model, @RequestParam("id")Integer userId, HttpSession s) {
				
		model.addAttribute("cartItems", userUtils.getCart( userId));
		
		s.setAttribute("vat", userUtils.getVat());
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
