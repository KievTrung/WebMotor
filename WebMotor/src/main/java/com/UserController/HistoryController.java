package com.UserController;

import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
