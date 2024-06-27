package com.UserController;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Auxiliaries.OrderInformation;
import com.Entity.Account;
import com.Utils.AdminUtils;
import com.Utils.UserUtils;

@Controller
public class HistoryController {
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
	
	@RequestMapping("repay")
	public String repay(ModelMap model, @RequestParam("orderId")Integer exportBillId, HttpSession s) throws ParseException {

		s.setAttribute("donHang", adminUtils.getBill(exportBillId, "Export"));
		
		model.addAttribute("metaInfo", new OrderInformation());
		s.setAttribute("repayFlag", true);
		return "User/order";
	}
	
	@RequestMapping("history")
	public String getHistory(ModelMap model, HttpSession s) throws ParseException {
		model.addAttribute("exportBills", userUtils.getExportBillsBelongToUser(((Account)s.getAttribute("account")).getId()));
		return "User/history";
	}
}
