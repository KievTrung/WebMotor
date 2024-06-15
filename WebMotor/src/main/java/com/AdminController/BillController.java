package com.AdminController;

import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Enums.SortType;
import com.Utils.AdminUtils;
import com.Utils.UserUtils;

@Controller
@RequestMapping("admin")
public class BillController {
	@Autowired
	AdminUtils adminUtils;
	
	@Autowired
	UserUtils userUtils;
	
	private int defaultPageIndex = 0;
	private String defaultBillType = "All";
	private SortType defaultSortType = SortType.HIGH2LOW;
	private String defaultKeywordCode = null;
	
	@RequestMapping("bill")
	public String getBill(ModelMap model, HttpSession s) {
		try {
			model.addAttribute("bills", adminUtils.getBillList(defaultPageIndex, defaultSortType, defaultBillType, defaultKeywordCode));
			model.addAttribute("latest", "selected");
			
			s.removeAttribute("numberOfBillPage");
			s.removeAttribute("numberOfBill");
			s.removeAttribute("sortType");
			s.removeAttribute("billType");
			s.removeAttribute("soHoaDon");
			
			long numberOfBill = adminUtils.getNumberOfBills(defaultBillType, defaultKeywordCode);
			s.setAttribute("numberOfBill", numberOfBill);
			s.setAttribute("numberOfBillPage", userUtils.getNumberOfItemPages(numberOfBill, adminUtils.getItemsPerPage()));
			s.setAttribute("billPageIndex", defaultPageIndex);
			s.setAttribute("billType", defaultBillType);
			s.setAttribute("sortType", defaultSortType);
			s.setAttribute("soHoaDon", defaultKeywordCode);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "Admin/Bill/bills";
	}
	
	@RequestMapping("billPageIndex")
	public String pageIndex(ModelMap model, HttpSession s, @RequestParam("i")int billPageIndex) {
		
		s.setAttribute("billPageIndex", billPageIndex);
		
		SortType sType = (SortType)s.getAttribute("sortType");		
		switch(sType) {
		case LOW2HIGH:
			model.addAttribute("oldest", "selected");
			break;
		case HIGH2LOW:
		case A2Z: case Z2A:
		default:
			model.addAttribute("latest", "selected");
		}
		
		String billType = (String)s.getAttribute("billType");
		switch(billType) {
		case "Import":
			model.addAttribute("Import", "selected");
			break;
		case "Export":
			model.addAttribute("Export", "selected");
			break;
		case "All":
			model.addAttribute("All", "selected");
		}
		
		try {
			model.addAttribute("bills", adminUtils.getBillList(billPageIndex, sType, billType, (String)s.getAttribute("soHoaDon")));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "Admin/Bill/bills";
	}
	
	@RequestMapping(value="billFilter", method=RequestMethod.POST)
	public String searchBill(ModelMap model, HttpSession s,
								@RequestParam("soHoaDon") String soHoaDon, 
								@RequestParam("sort")SortType sortType,
								@RequestParam("type")String billType) {

		s.setAttribute("soHoaDon", soHoaDon);
		s.setAttribute("sortType", sortType);
		s.setAttribute("billType", billType);
		try {
			long numberOfBill = adminUtils.getNumberOfBills(billType, soHoaDon);
			s.setAttribute("numberOfBill", numberOfBill);
			s.setAttribute("numberOfBillPage", userUtils.getNumberOfItemPages(numberOfBill, adminUtils.getItemsPerPage()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "redirect:/admin/billPageIndex?i=0";
	}
}
