package com.AdminController;

import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Entity.Account;
import com.Utils.AdminUtils;
import com.Utils.CSession;
import com.Utils.UserUtils;

@Controller
@RequestMapping("admin")
public class UserDetailController {
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	AdminUtils adminUtils;
	
	@RequestMapping("userDetail")
	public String getUserDetail(ModelMap model, @RequestParam("name")String username) {
		//get user information
		Account user = userUtils.getUser(username);
		model.addAttribute("user", user);
		try {
			//get user import list
			model.addAttribute("importBills", adminUtils.getImportExportBillBelongToUser(user.getId(), true));
			//get user export list
			model.addAttribute("exportBills", adminUtils.getImportExportBillBelongToUser(user.getId(), false));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "Admin/UserManagement/userDetail";
	}
	
	@RequestMapping("cart")
	public String getCart(@RequestParam("id")Integer userId) {
		return "redirect:/cart?id=" + userId;
	}
	
	@RequestMapping("activateUser")
	public String activateUser(@RequestParam("id")Integer userId, 
								@RequestParam("name")String username,
								RedirectAttributes ra) {
		try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
			
			Transaction tran = csession.getSession().beginTransaction();
			try {
				Query q = csession.getSession().createQuery("update Account set isActive = 1 where id = :id");
				
				q.setParameter("id", userId);
				
				q.executeUpdate();
				tran.commit();
			} catch (Exception e) {
				tran.rollback();
				e.printStackTrace();
				ra.addFlashAttribute("msg", "alert('"+ e.getMessage()  +"')");
			}
		}
		return "redirect:/admin/userDetail?name=" + username;
	}
	
	@RequestMapping("deactivateUser")
	public String deactivateUser(@RequestParam("id")Integer userId, 
								@RequestParam("name")String username, 
								HttpSession s, 
								RedirectAttributes ra) {
		if(((Account)s.getAttribute("account")).getId() == userId)
			ra.addFlashAttribute("msg", "alert('You can not deactivate yourself')");
		else {
			try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
				
				Transaction tran = csession.getSession().beginTransaction();
				try {
					Query q = csession.getSession().createQuery("update Account set isActive = 0 where id = :id");
					
					q.setParameter("id", userId);
					
					q.executeUpdate();
					tran.commit();
				} catch (Exception e) {
					tran.rollback();
					e.printStackTrace();
					ra.addFlashAttribute("msg", "alert('"+ e.getMessage()  +"')");
				}
			}
		}
		return "redirect:/admin/userDetail?name=" + username;
	}
	
	@RequestMapping("changeToUser")
	public String changeToUser(@RequestParam("id")Integer userId, 
								@RequestParam("name")String username,
								HttpSession s, 
								RedirectAttributes ra) {
		if(((Account)s.getAttribute("account")).getId() == userId)
			ra.addFlashAttribute("msg", "alert('You can not change to user role')");
		else 
		{
			try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
				
				Transaction tran = csession.getSession().beginTransaction();
				try {
					Query q = csession.getSession().createQuery("update Account set isAdmin = 0 where id = :id");
					
					q.setParameter("id", userId);
					
					q.executeUpdate();
					tran.commit();
				} catch (Exception e) {
					tran.rollback();
					e.printStackTrace();
					ra.addFlashAttribute("msg", "alert('"+ e.getMessage()  +"')");
				}
			}
		}
		return "redirect:/admin/userDetail?name=" + username;
	}
	
	@RequestMapping("changeToAdmin")
	public String changeToAdmin(@RequestParam("id")Integer userId, 
								@RequestParam("name")String username,
								RedirectAttributes ra) {		
		try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
			
			Transaction tran = csession.getSession().beginTransaction();
			try {
				Query q1 = csession.getSession().createQuery("update Account set isAdmin = 1 where id = :id");
				Query q2 = csession.getSession().createQuery("delete GioHang where id.user.id = :id");
				
				q1.setParameter("id", userId);
				q2.setParameter("id", userId);
				
				q1.executeUpdate();
				q2.executeUpdate();
				tran.commit();
			} catch (Exception e) {
				tran.rollback();
				e.printStackTrace();
				ra.addFlashAttribute("msg", "alert('"+ e.getMessage()  +"')");
			}
		}
		return "redirect:/admin/userDetail?name=" + username;
	}
}
