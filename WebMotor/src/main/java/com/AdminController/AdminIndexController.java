package com.AdminController;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Auxiliaries.Bill;
import com.Utils.AdminUtils;

@Controller
@RequestMapping("admin")
public class AdminIndexController {
	@Autowired
	AdminUtils adminUtils;
	
	@RequestMapping("index")
	public String index(HttpSession s) {
		s.setAttribute("vat", adminUtils.getVat());
		s.setAttribute("user", adminUtils.getUserCount());
		s.setAttribute("admin", adminUtils.getAdminCount());
		return "Admin/index";
	}
	
	@RequestMapping("profile")
	public String profile() {
		return "redirect:/profile";
	}
	
	@RequestMapping("modifyVat")
	public String modifyVat(ModelMap model, @RequestParam("vat")Double vat, RedirectAttributes ra) {
		if(vat < 0)
			ra.addFlashAttribute("msg", "alert('VAT can not be negative')");
		else {
			adminUtils.updateVat(vat);
			ra.addFlashAttribute("msg", "alert('Update vat successfully')");			
		}
		return "redirect:/admin/index";
	}
	
	@RequestMapping(value="calRevenue", method=RequestMethod.POST)
	public String calculateRevenue(ModelMap model, 
						@RequestParam("start")String start, 
						@RequestParam("end")String end) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = formatter.parse(start);
			Date endDate = formatter.parse(end);
			//check if date is valid
			if(startDate.after(endDate) || startDate.equals(endDate)){
				model.addAttribute("msg", "alert('Start date and end date invalid')");
				return "Admin/index";
			}

			//get export bills from startDate to endDate
			List<Bill> exportBills = adminUtils.getExportBillFromStartToEnd(startDate, endDate);
			model.addAttribute("exportBills", exportBills);
			//caculate total from export bills
			model.addAttribute("totalExport", adminUtils.caculateTotalExportBills(exportBills));
			
			//do it again with import bills
			List<Bill> importBills = adminUtils.getImportBillFromStartToEnd(startDate, endDate);
			model.addAttribute("importBills", importBills);
			model.addAttribute("totalImport", adminUtils.caculateTotalImportBills(importBills));

		} catch (SQLException | ParseException e) {
			e.printStackTrace();
			model.addAttribute("msg", "alert('"+ e.getMessage() +"')");
		}
		
		return "Admin/index";
	}
	
}
