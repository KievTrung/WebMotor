package com.AdminController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Auxiliaries.Product;
import com.Utils.AdminUtils;
import com.Utils.CSession;

@Controller
@RequestMapping("admin")
public class AdminProductController {
	@Autowired
	ServletContext context;
	
	@Autowired
	AdminUtils adminUtils;
	
	@ModelAttribute("type")
	public List<String> loadCategory(){
		return adminUtils.getCategory();
	}
	
	@RequestMapping("product")
	public String getDefaultPage() {
		return "redirect:/product";
	}
	
	@RequestMapping("adminProductDetail")
	public String getProductDetail(ModelMap model, @RequestParam("id")String productId, HttpSession s){
		
		//fetch product information
		Product product = new Product();
		product.setCode(productId);
		product.setAmount(adminUtils.getProductStocks(productId));
		model.addAttribute("stock", product.getAmount());
		model.addAttribute("product", adminUtils.fetchProductInfoFromDb(product));
		
		s.removeAttribute("productImages");
		s.removeAttribute("bills");
		
		s.setAttribute("productImages", product.getPics());
		
		//fetch bills reference product
		try {
			s.setAttribute("bills", adminUtils.getBillsReferenceProduct(productId));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Admin/AdminProductDetail";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("saveProduct")
	public String saveProduct( @Valid @ModelAttribute("product")Product product,
							@RequestParam("images") MultipartFile[] newImages, 
							@RequestParam("currenType") String category ,
							HttpSession session,
							BindingResult error,
							RedirectAttributes rd) throws IllegalStateException, IOException, SQLException {
		//check for error
		boolean errorFlag = false;

		if(product.getName().equals("")) {
			error.rejectValue("name", "2", "Invalid name");
			errorFlag = true;
		}
		
		if(product.getAmount() == null || product.getAmount() <= 0) {
			error.rejectValue("amount", "2", "Invalid amount");
			errorFlag = true;
		}
		
		if(product.getPrice() == null || product.getPrice() <= 0) {
			error.rejectValue("price", "2", "Invalid price");
			errorFlag = true;
		}
		
		if(product.getType().equals("") && category.equals("none")) {
			error.rejectValue("type", "3", "Invalid category");
			errorFlag = true;
		}
		
		if(product.getType().equals("")) 
			product.setType(category);
		
		if(errorFlag) return "Admin/AdminProductDetail";
		
		product.setPics((List<String>)session.getAttribute("productImages"));
		
		//check if new images uploadable
		List<String> currentImages = product.getPics();
		if(!newImages[0].isEmpty() && adminUtils.isUploadable(currentImages, newImages))
			//insert new images to product
			product.setPics(adminUtils.insertProductImages(currentImages, newImages));
		else
			rd.addFlashAttribute("msg", "alert('Failed to upload : only "+ adminUtils.getImagesUploadedLimit()  +" images is alow')");
		
		//update product new information to db
		String maXe = product.getCode();
		try(Connection con = adminUtils.getJdbc().getConnection()){
			//disable auto commit
			con.setAutoCommit(false);	
			try {				
				maXe = adminUtils.updateProduct(con, product, false);
				con.commit();
			}
			catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		}
		return "redirect:/admin/adminProductDetail?id=" + maXe;
	}
	
	@RequestMapping("deleteImageFromEditProduct")
	public String deleteImage(@RequestParam("image")String image,
							@RequestParam("productId")String productId,
							@RequestParam("quantity")Integer quantity,
								RedirectAttributes rd) {
		//if there is only 1 image in list then can not delete image
		if(quantity == 1) {
			rd.addFlashAttribute("msg", "alert('Image can not deleted')");
			return "redirect:/admin/adminProductDetail?id=" + productId;
		}
		//delete pic from db		
		adminUtils.deleteDbImage(productId, image);
		//delete pic from directory
		adminUtils.deleteResourcesImage(image);
		return "redirect:/admin/adminProductDetail?id=" + productId;
	}
	
	@RequestMapping("activeProduct")
	public String activeProduct(@RequestParam("id")String productId) {
		try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
			Query q = csession.getSession().createQuery("update ChiTietXe set isActive = 1 where maXe = :maXe");
			q.setParameter("maXe", productId);
			q.executeUpdate();
		}
		return "redirect:/admin/adminProductDetail?id=" + productId;
	}
	
	@RequestMapping("deactiveProduct")
	public String deactiveProduct(@RequestParam("id")String productId) {
		
		if(((String)context.getAttribute("frontPage")).equals(productId))
			context.removeAttribute("frontPage");
		
		if(((String)context.getAttribute("specialOffer")).equals(productId))
			context.removeAttribute("specialOffer");
		
		try(CSession csession = new CSession(adminUtils.getSessionFactory().openSession())){
			Query q = csession.getSession().createQuery("update ChiTietXe set isActive = 0 where maXe = :maXe");
			q.setParameter("maXe", productId);
			q.executeUpdate();
		}
		return "redirect:/admin/adminProductDetail?id=" + productId;
	}
	
	@RequestMapping("setFrontPage")
	public String setFrontPage(@RequestParam("id")String productId) {
		context.setAttribute("frontPage", productId);
		return "redirect:/admin/adminProductDetail?id=" + productId;
	}
	
	@RequestMapping("setSpecialOffer")
	public String setSpecialOffer(@RequestParam("id")String productId) {		
		context.setAttribute("specialOffer", productId);
		return "redirect:/admin/adminProductDetail?id=" + productId;
	}
	
}
