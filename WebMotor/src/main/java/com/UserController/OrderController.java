package com.UserController;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Auxiliaries.Bill;
import com.Auxiliaries.OrderInformation;
import com.Entity.Account;
import com.Enums.PaymentMethod;
import com.Utils.AdminUtils;
import com.Utils.UserUtils;


@Controller
public class OrderController {

	@Autowired
	UserUtils userUtils;
	
	@Autowired
	AdminUtils adminUtils;
	
	@ModelAttribute("year")
	public Map<Integer, String> getYear(){
		Map<Integer, String> year = new HashMap<Integer, String>();
		year.put(0, "Year");

		LocalDate currentDate = LocalDate.now();
		int currentYear = currentDate.getYear();
		
		for(int i=0; i<11; i++)
			year.put(currentYear - 2000 - i, String.valueOf(currentYear - i));
		
		return year;
	}

	@ModelAttribute("month")
	public Map<Integer, String> getMonth(){
		Map<Integer, String> month = new HashMap<Integer, String>();
		month.put(0, "Month");
		month.put(1, "January");
		month.put(2, "February");
		month.put(3, "March");
		month.put(4, "April");
		month.put(5, "May");
		month.put(6, "June");
		month.put(7, "July");
		month.put(8, "August");
		month.put(9, "September");
		month.put(10, "October");
		month.put(11, "November");
		month.put(12, "December");
		return month;
	}
	
	@RequestMapping("createOrder")
	public String createOrder(ModelMap model, HttpSession s, RedirectAttributes ra) {
		int userId = ((Account)s.getAttribute("account")).getId();
		
		try {
			//create export bill
			int exportBillId = userUtils.createExportBill(userId, PaymentMethod.NA.toString());
			//load export bill to user
			s.setAttribute("donHang", adminUtils.getBill(exportBillId, "Export"));
			//get user infor
			model.addAttribute("metaInfo", new OrderInformation());
		} catch (Exception e) {
			e.printStackTrace();
			ra.addFlashAttribute("msg", "alert('Error: "+ e.getMessage() +"')");
			return "redirect:/cart?id=" + userId;
		}
		return "User/order";
	}
	
	@RequestMapping(value="placeOrder", method=RequestMethod.POST)
	public String placeOrder(@Valid @ModelAttribute("metaInfo")OrderInformation meta, BindingResult errors,
							@RequestParam("paymentMethod")PaymentMethod pm,
							ModelMap model,
							HttpSession s,
							RedirectAttributes rd) {
		
		if(pm == PaymentMethod.NA) 
		{
			model.addAttribute("errorMsg", "Please choose a payment method");
			return "User/order";
		}
		else if(pm == PaymentMethod.VISA) 
		{
			boolean hasError = errors.hasErrors();
			
			if(!userUtils.checkDate(meta.getMonth(), meta.getYear())) {
				errors.rejectValue("year", "8", "Invalid date");				
				hasError = true;
			}
			
			if(hasError) {
				model.addAttribute("onload", "displayError()");	
				return "User/order";
			}
		}
		
		int exportBillId = ((Bill)s.getAttribute("donHang")).getSoHoaDon();
		
		try {
			userUtils.placeOrder(exportBillId, pm, meta);
		}catch(Exception ex) {
			rd.addFlashAttribute("errorOrderMsg", ex.getCause().getMessage());
			ex.printStackTrace();
			return "redirect:/cart?id=" + ((Account)s.getAttribute("account")).getId();
		}
		
		//delete every thing in cart
		if(s.getAttribute("repayFlag") == null)
			userUtils.removeCart(((Account)s.getAttribute("account")).getId());
		else
			s.removeAttribute("repayFlag");
		
		return "redirect:/history";	
	}
	
	@RequestMapping("cancelOrder")
	public String cancelOrder(HttpSession s){
		//remove everything in cart
		if(s.getAttribute("repayFlag") == null)
			userUtils.removeCart(((Account)s.getAttribute("account")).getId());
		else
			s.removeAttribute("repayFlag");
		

		s.removeAttribute("donHang");
		return "redirect:/history";
	}

	
	@RequestMapping("deleteOrder")
	public String deleteOrder(ModelMap model, @RequestParam("orderId")int orderId) {
		userUtils.deleteDonHang(orderId);
		return "redirect:/history";
	}
}
