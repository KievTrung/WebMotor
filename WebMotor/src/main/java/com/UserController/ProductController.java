package com.UserController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Entity.Account;
import com.Enums.SortType;
import com.Utils.UserUtils;


@Controller
public class ProductController {
	@Autowired
	UserUtils userUtils;
	
	private int defaultPageIndex = 0;
	private String defaultVehicleType = "All";
	private SortType defaultSortType = SortType.A2Z;
	private String defaultKeyword = "";
	
	@RequestMapping("product")
	public String getDefaultPage(HttpSession s, ModelMap model){
		
		//get a list of defaultPageIndex and put it in jsp 
		boolean isAdmin = isAdmin(s);
		model.addAttribute("productList", userUtils.getChiTietXeList(defaultPageIndex, defaultSortType, defaultVehicleType, defaultKeyword, isAdmin));
		model.addAttribute("productType", "All products");
		model.addAttribute("a2z", "selected");
		
		s.removeAttribute("numberOfProductPages");
		s.removeAttribute("numberOfProducts");
		s.removeAttribute("sortType");
		s.removeAttribute("vehicleType");
		s.removeAttribute("keyword");
		
		long numberOfProducts = userUtils.getNumberOfProducts(defaultVehicleType, defaultKeyword, isAdmin);
		s.setAttribute("numberOfProducts", numberOfProducts);
		s.setAttribute("numberOfProductPages", userUtils.getNumberOfItemPages(numberOfProducts, userUtils.getItemsPerPage()));
		s.setAttribute("productPageIndex", defaultPageIndex);
		s.setAttribute("sortType", defaultSortType);
		s.setAttribute("vehicleType", defaultVehicleType);
		s.setAttribute("keyword", defaultKeyword);
		
		defaultVehicleType = "All";
		
		return "Product/products";
	}

	
	@RequestMapping("pageIndex")
	public String getPage(ModelMap model, HttpSession s, @RequestParam("i") int pageIndex) {
		
		s.setAttribute("productPageIndex", pageIndex);
		
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
		model.addAttribute("productList", userUtils.getChiTietXeList(pageIndex, sType, (String)s.getAttribute("vehicleType"), (String)s.getAttribute("keyword"), isAdmin(s)));
		
		return "Product/products";
	}
	
	@RequestMapping(value="filter", method=RequestMethod.POST)
	public String filterProduct(ModelMap model, 
								@RequestParam("type")String vType,
								@RequestParam("sort")SortType sType,
								@RequestParam("key")String keyword,
								HttpSession s) {
		
		long numberOfProducts = userUtils.getNumberOfProducts(vType, keyword, isAdmin(s));
		s.setAttribute("numberOfProductPages", userUtils.getNumberOfItemPages(numberOfProducts, userUtils.getItemsPerPage()));
		s.setAttribute("keyword", keyword.trim());
		
		s.setAttribute("vehicleType", vType);
		s.setAttribute("sortType", sType);
				
		return "redirect:/pageIndex?i=0";
	}
	
	@RequestMapping("category")
	public String getCategory(ModelMap model, @RequestParam("category")String vehicleType) {
		defaultVehicleType = vehicleType;
		return "redirect:/product";
	}

	@ModelAttribute("productTypeList")
	public List<String> getItems(){
		return userUtils.getLoaiXe();
	}

	private boolean isAdmin(HttpSession s) {
		Account account = (Account)s.getAttribute("account");
		return (account != null && account.isAdmin())? true : false; 
	}
	
}
