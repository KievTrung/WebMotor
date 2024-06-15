package com.AdminController;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Utils.AdminUtils;
import com.Utils.CSession;

@Controller
@RequestMapping("admin")
public class BillDetailController {
	@Autowired
	AdminUtils adminUtils;
	
	@RequestMapping("billDetail")
	public String getBillDetail(ModelMap model, 
								@RequestParam("id")Integer soHoaDon,
								@RequestParam("type")String type) {
		model.addAttribute("bill", adminUtils.getBill(soHoaDon, type));
		return "Admin/Bill/billDetail";
	}
	
	//change payment state
	@RequestMapping("changeState")
	public String changeState(@RequestParam("id")Integer soHoaDon,
							@RequestParam("val")Boolean trangThaiThanhToan) {
		
		try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){			
			Query q = csession.getSession().createQuery(" update DonHang "
														+ " set trangThaiThanhToan = " + ((trangThaiThanhToan) ? "0" : "1")
														+ " where soHoaDon = :id");
			q.setParameter("id", soHoaDon);
			q.executeUpdate();
		}
		return "redirect:/admin/billDetail?type=Export&id=" + soHoaDon;
	}
	
}
