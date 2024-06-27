package com.AdminController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.Entity.Account;
import com.Utils.AdminUtils;

@Controller
@RequestMapping("admin")
public class ImportController {
	@Autowired
	AdminUtils adminUtils;
	
	private List<Product> importList = new ArrayList<Product>();
	
	@ModelAttribute("importList")
	public List<Product> getImportList(){
		return importList;
	}
	
	@ModelAttribute("type")
	public List<String> loadCategory(){
		return adminUtils.getCategory();
	}
	
	@RequestMapping("import")
	public String importProduct(ModelMap model, HttpSession session) {
		model.addAttribute("product", new Product());
		session.removeAttribute("editMode");
		session.removeAttribute("productImages");
		return "Admin/Bill/import";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("addProduct")
	public String addImportProduct(ModelMap model, 
							@Valid @ModelAttribute("product")Product product,
							@RequestParam("images") MultipartFile[] newImages, 
							@RequestParam("currenType") String category ,
							HttpSession session,
							BindingResult error,
							RedirectAttributes rd) throws IllegalStateException, IOException {
		
		boolean errorFlag = false;
		
		if(product.getName().equals("")) {
			error.rejectValue("name", "2", "Invalid name");
			errorFlag = true;
		}
		
		if(product.getAmount() == null || product.getAmount() <= 0) {
			error.rejectValue("amount", "2", "Invalid amount");
			errorFlag = true;
		}

		product.setIsNew(!adminUtils.isInDb(product.getName()));
		
		if(product.isNew()) {				
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
		}
		
		if(errorFlag) return "Admin/Bill/import";
		
		product.setPics((List<String>)session.getAttribute("productImages"));

		//get product index in list
		int index = adminUtils.isInList(product.getName(), importList);
		
		//if product in list
		if(index != -1) {			
			//if this in edit mode
			if(session.getAttribute("editMode") != null) {
				//double check price
				if(product.getPrice() == null || product.getPrice() <= 0) {
					error.rejectValue("price", "2", "Invalid price");
					return "Admin/Bill/import";
				}
				else
					rd.addFlashAttribute("msg", "alert('"+ adminUtils.saveProductToList(product, newImages, importList, index) + "')");
			}
			else
				rd.addFlashAttribute("msg", "alert('Product already in the list')");
				
			
			return "redirect:/admin/import";
		}
		else 
		{			
			Product fetchProduct = adminUtils.fetchProductInfoFromDb(product);
			//if product in db
			if(fetchProduct != null) 
			{				
				List<String> pics = fetchProduct.getPics();
				
				//insert new image 
				if(adminUtils.isUploadable(pics, newImages))
					fetchProduct.setPics(adminUtils.insertProductImages(pics, newImages));
				else
					rd.addFlashAttribute("msg", "alert('Warning: Upload reach over "+ adminUtils.getImagesUploadedLimit() + "')");
					
				//add to import list
				importList.add(fetchProduct);			
			}
			else //if not, meaning this is new product 
			{	
				if(newImages[0].isEmpty()) {
					model.addAttribute("uploadError", "Please upload at least 1 image");
					errorFlag = true;
				}
				
				if(!errorFlag) {
					//insert new images to product images list
					product.setPics(adminUtils.insertProductImages(product.getPics(), newImages));
					//pick the first image in product images list to be the thumnail
					product.setPicture(product.getPics().get(0));
					importList.add(product);				
				}
				else
					return "Admin/Bill/import";
			}
		}
		return "redirect:/admin/import";
	}
	
	@RequestMapping("editProduct")
	public String editImportProduct(ModelMap model, 
									@RequestParam("index")int index, 
									HttpSession session) {
		session.setAttribute("editMode", true);
		Product product = importList.get(index);
		model.addAttribute("product", product);
		session.setAttribute("productImages", product.getPics());
		model.addAttribute("productIndex", index);
		
		return "Admin/Bill/import";
	}
	
	@RequestMapping("deleteProduct")
	public String deleteImportProduct(ModelMap model, @RequestParam("name")String name) {
		for(int i=0; i<importList.size(); i++) {
			Product product = importList.get(i);
			if(product.getName().equals(name)) {
				adminUtils.deleteImages(product.getPics());
				importList.remove(i);				
			}
		}
		return "redirect:/admin/import";
	}

	@RequestMapping("deleteImageFromImportProduct")
	public String deleteImage(ModelMap model, 
								@RequestParam("name")String name,
								@RequestParam("productIndex")int index,
								RedirectAttributes rd) {
		//get product in list
		Product product = importList.get(index);
		//get pic list from product
		List<String> pics = product.getPics();
		//is this picture deletable
		if(!adminUtils.isImageDeletable(name) || pics.size() == 1) {
			rd.addFlashAttribute("msg", "alert('Image can not deleted')");
			return "redirect:/admin/editProduct?index=" + index;
		}
		//remove specify image
		pics.remove(name);
		//set new thumnail for product in the case that delete the thumnail
		product.setPicture(pics.get(0));
		//replace pic list in product
		product.setPics(pics);
		//replace product in list
		importList.set(index, product);
		//delete pic from directory
		adminUtils.deleteUploadImage(name);
		return "redirect:/admin/editProduct?index=" + index;
	}
	
	@RequestMapping("createImport")
	public String createImportList(ModelMap model, HttpSession s, RedirectAttributes rd) {
		int soHoaDon = -1;
		
		if(importList.isEmpty()){
			rd.addFlashAttribute("msg", "alert('Can not create import, error: Empty list')");
			return "redeirect:/admin/import";
		}
		
		try {
			soHoaDon = adminUtils.createImport(importList, ((Account)s.getAttribute("account")).getId());
			importList.clear();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			rd.addFlashAttribute("msg", "alert('Can not create import, error: " + e.getMessage() + "')");
			return "redeirect:/admin/import";
		}
		return "redirect:/admin/billDetail?id=" + soHoaDon + "&type=Import";
	}
}
