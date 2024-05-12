package com.Controller;


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

import com.Beans.Order;
import com.Beans.OrderInformation;
import com.Entity.Account;
import com.Enums.PaymentMethod;
import com.Utils.UserUtils;


@Controller
public class OrderController {

	@Autowired
	UserUtils userUtils;
	
	@RequestMapping("createOrder")
	public String createOrder(ModelMap model, HttpSession s) {
		int userId = ((Account)s.getAttribute("account")).getId();

		int orderId = userUtils.taoDonHang(userId, PaymentMethod.NA.toString());
		
		s.setAttribute("donHang", userUtils.getOrder(orderId));
		
		model.addAttribute("metaInfo", new OrderInformation());
		return "User/order";
	}
	
	@RequestMapping("history")
	public String getHistory(ModelMap model, HttpSession s) {
		int userId = ((Account)s.getAttribute("account")).getId();
		model.addAttribute("orders", userUtils.getListOrder( userId));
		return "User/history";
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
			if(errors.hasErrors()) {
				model.addAttribute("onload", "displayError()");			
				return "User/order";
			}
			if(!checkDate(meta.getMonth(), meta.getYear())) {
				errors.rejectValue("year", "8", "Invalid date");
				return "User/order";
			}
		}
		
		int orderId = ((Order)s.getAttribute("donHang")).getOrderId();
		
		try {
			userUtils.placeOrder(orderId, pm, meta);
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
	
	private boolean checkDate(int month, int year) {
		
		if(month == 0 || year == 0)
			return false;
		
		LocalDate current = LocalDate.now();
		LocalDate date = LocalDate.of(year, month, 1);
		
		return (date.isBefore(current) || date.isEqual(current)) ? true : false;
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

	@RequestMapping("repay")
	public String repay(ModelMap model, @RequestParam("orderId")int orderId, HttpSession s) {

		s.setAttribute("donHang", userUtils.getOrder( orderId));
		
		model.addAttribute("metaInfo", new OrderInformation());
		s.setAttribute("repayFlag", true);
		return "User/order";
	}
	
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
	
//	@RequestMapping("test")
//	public String repay(ModelMap model) {
//		Session session = sf.openSession();
//		ProcedureCall pc = session.createStoredProcedureCall("sp_tr_updateStock");
//		pc.registerParameter("param", int.class, ParameterMode.IN).bindValue(21);;
//		
//		ProcedureOutputs output = null;	
//		ResultSetOutput result = null;
//		try {
//			output = pc.getOutputs();	
//		}catch(Exception ex) {
//			System.out.println(ex.getCause());
//			ex.printStackTrace();
//			return "test";
//		}
//		try {
//			result = (ResultSetOutput)output.getCurrent();
//			
//		}catch(Exception ex) {
//			System.out.println("2");
//			System.out.println(ex.getCause());
//			return "test";
//		}
//		try {
//			List<Integer> a = (List<Integer>)result.getResultList();
//			
//		}catch(Exception ex) {
//			System.out.println("3");
//			System.out.println(ex.getMessage());
//		}
//		
//		session.close();
//		return "test";
//	}
}
