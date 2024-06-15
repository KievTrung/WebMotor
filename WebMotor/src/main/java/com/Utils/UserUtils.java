 package com.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Auxiliaries.Bill;
import com.Auxiliaries.OrderInformation;
import com.Auxiliaries.Product;
import com.Entity.Account;
import com.Enums.AFlag;
import com.Enums.PaymentMethod;
import com.Enums.SortType;

@Component("userUtils")
public class UserUtils {
	@Autowired
	SessionFactory sessionFactory;
	
	private String email = "n21dccn004@student.ptithcm.edu.vn";
	private int itemsPerPage = 8;

	public String getEmail() {
		return email;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public int datHang( int id, String hinhThucThanhToan) {
		int result = 0;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_datHang");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(id);
			pc.registerParameter("hinhThucThanhToan", String.class, ParameterMode.IN).bindValue(hinhThucThanhToan);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		return result;
	}
	
	public void placeOrder(int soHoaDon, PaymentMethod pm, OrderInformation orderInfo) throws Exception, TransactionException{
		orderInfo.setCardNumber(orderInfo.getCardNumber().replaceAll("\\s", ""));
		orderInfo.setCardName(orderInfo.getCardName().trim());		
		
		String additionInfo = "";
		switch(pm) {
		case VISA:
			additionInfo += orderInfo.getCardNumber() + "-" +
					orderInfo.getMonth() + "/" + 
					orderInfo.getYear() + "-" +
					orderInfo.getCvv() + "-" + 
					orderInfo.getCardName();
			break;
		case INPLACE: case NA:
			additionInfo += "null";
		}
		
		//check if there are still enough stocks in db and update them
		try
		{
			updateStock(soHoaDon);
		}catch(Exception ex) {
			restoreDonHang(soHoaDon);
			deleteDonHang(soHoaDon);
			throw ex;
		}
		
		Transaction tran = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			
			tran = csession.getSession().beginTransaction();

			//update order query
			String hql = " update DonHang "
					+ " set additionInfo = :ai, hinhThucThanhToan = :pm, diaChi = :addr, trangThaiThanhToan = 1 "
					+ " where soHoaDon = :orderId";
			
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("orderId", soHoaDon);
			q.setParameter("pm", pm.toString());
			q.setParameter("addr", orderInfo.getAddress());
			q.setParameter("ai", additionInfo);			
			q.executeUpdate();
			tran.commit();
		}
		catch(Exception ex) {
			tran.rollback();
			throw ex;
		}

	}
	
	public void updateStock(int soHoaDon) throws Exception{
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_updateStock");
			pc.registerParameter("soHoaDon", int.class, ParameterMode.IN).bindValue(soHoaDon);
			pc.getOutputs();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
		
	public void restoreDonHang(int soHoaDon) throws Exception{
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_restoreDonHang");
			pc.registerParameter("soHoaDon", int.class, ParameterMode.IN).bindValue(soHoaDon);
			pc.getOutputs();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getCart( int userId) {
		String hql = "select gh.id.xe.maXe, gh.id.xe.tenXe, gh.id.xe.giaXe, gh.soLuong, min(vp.id.ten)"
				+ " from GioHang gh "
				+ " inner join gh.id.xe.vehiclePictures vp "
				+ " where gh.id.user.id = :id "
				+ " group by gh.id.xe.maXe, gh.id.xe.tenXe, gh.id.xe.giaXe, gh.soLuong";
		try (CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("id", userId);
			List<Object[]> list = (List<Object[]>)q.list();			

			List<Product> cart = new ArrayList<Product>();
			for(Object[] obj : list) {
				Product product = new Product();
				
				product.setCode(obj[0].toString());
				product.setName(obj[1].toString());
				product.setPrice(Integer.parseInt(obj[2].toString()));
				product.setAmount(Integer.parseInt(obj[3].toString()));
				product.setPicture(obj[4].toString());
				
				cart.add(product);
			}
			return cart;
		}
	}
	
	public int setUpTaiKhoan( int flag, String login, String password, int phone, String email, String diaChi, boolean isAdmin, boolean isActive) {
		int result = 0;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_setUpTaiKhoan");
			pc.registerParameter("login_name", String.class, ParameterMode.IN).bindValue(login);
			pc.registerParameter("password", String.class, ParameterMode.IN).bindValue(password);
			pc.registerParameter("phone", int.class, ParameterMode.IN).bindValue(phone);
			pc.registerParameter("email", String.class, ParameterMode.IN).bindValue(email);
			pc.registerParameter("diaChi", String.class, ParameterMode.IN).bindValue(diaChi);
			pc.registerParameter("isAdmin", boolean.class, ParameterMode.IN).bindValue(isAdmin);
			pc.registerParameter("isActive", boolean.class, ParameterMode.IN).bindValue(isActive);
			pc.registerParameter("flag", int.class, ParameterMode.IN).bindValue(flag);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public int themGioHang( int id, String maXe, int soLuong) {
		int result = 0;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_themGioHang");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(id);
			pc.registerParameter("maXe", String.class, ParameterMode.IN).bindValue(maXe);
			pc.registerParameter("soLuong", int.class, ParameterMode.IN).bindValue(soLuong);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public Long countCartItems( int id) {
		String hql = "select sum(soLuong) from GioHang where id.user.id = :id group by id.user.id";
		Long result = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("id", id);
			result = (Long)q.uniqueResult();
		}
		return (result == null ? 0 : result);
	}
	
	public void removeCartItems( String productId, int userId) {
		String hql = "delete from GioHang where id.xe.maXe = :productId and id.user.id = :userId";

		Transaction tran = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			tran = csession.getSession().beginTransaction();
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("productId", productId);
			q.setParameter("userId", userId);
			q.executeUpdate();
			tran.commit();
		}catch(Exception ex) {
			tran.rollback();
			ex.printStackTrace();
		}
	}

	public void removeCart( int userId) {
		String hql = "delete from GioHang where id.user.id = :userId";
		Transaction tran = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			tran = csession.getSession().beginTransaction();
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("userId", userId);
		 	q.executeUpdate();
			tran.commit();
		}catch(Exception ex) {
			tran.rollback();
			ex.printStackTrace();
		}
	}
	
	public int changeItemAmountInCart( int userId, String maXe, int soLuong) {
		int result = 0;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_thayDoiSoLuongItemTrongGioHang");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(userId);
			pc.registerParameter("maXe", String.class, ParameterMode.IN).bindValue(maXe);
			pc.registerParameter("soLuongYeuCau", int.class, ParameterMode.IN).bindValue(soLuong);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public int addVehiclePicture( String maXe, String name) {
		int result = 0;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_addVehiclePicture");
			pc.registerParameter("maXe", String.class, ParameterMode.IN).bindValue(maXe);
			pc.registerParameter("name", String.class, ParameterMode.IN).bindValue(name);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public void changeLogin( int id, String newLogin) throws Exception{
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_changeLoginName");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(id);
			pc.registerParameter("newLogin", String.class, ParameterMode.IN).bindValue(newLogin);
			pc.getOutputs();
		}
	}
	
	public Account getUser( String login) {		
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery("from Account where loginName = :login");
			q.setParameter("login", login);
			return (Account)q.uniqueResult();				
		}
	}
	
	public Account getUser( String login, String password) {
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery("from Account where loginName = :login and password = :pass");
			q.setParameter("login", login);
			q.setParameter("pass", password);
			return (Account)q.uniqueResult();
		}
	}
		
	public Integer checkLogin( String login) {
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery("select id from Account where loginName = :login");
			q.setParameter("login", login);
			return (Integer)q.uniqueResult();
		}
	}
	
	public String checkEmail( String email) {
		
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery("select loginName from Account where email = :email");
			q.setParameter("email", email);
			return (String)q.uniqueResult();
		}
		
	}
	
	public Integer checkPhone( int phone) {
		
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery("select id from Account where phone = :phone");
			q.setParameter("phone", phone);
			return (Integer)q.uniqueResult();
		}
		
	}

	public long getNumberOfProducts(String vehicleType, String keyword, boolean isAdmin) {		
		String hql = "select count(maXe) from ChiTietXe where ";
		
		hql += (isAdmin ? " 1=1" : " isActive = 1 ");
		
		if(!keyword.equals("") || !vehicleType.equals("All")) {
			hql += " and ";
			hql += (keyword.equals("") ? " 1=1" : " tenXe like concat('%',:keyword,'%') ");
			hql += " and ";
			hql += (vehicleType.equals("All") ? " 1=1 " : " loaiXe = :vehicleType ");			
		}
		
		long result = 0;
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			if(!keyword.equals(""))
				q.setParameter("keyword", keyword);
			if(!vehicleType.equals("All"))
				q.setParameter("vehicleType", vehicleType);
			result = (long)q.uniqueResult();
		}
		return result;
	}

	public long getNumberOfItemPages(long numberOfItems, int itemsPerPages) {		
		double pages = numberOfItems / (double)itemsPerPages;
		return (pages - (long)pages) > 0 ? (long)(++pages) : ((long)pages == 0 ? (long)(++pages) : (long)pages);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Product> getChiTietXeList( int pageIndex, SortType sType, String vehicleType, String keyword, boolean isAdmin) {
		
		String hql = "select ctx.maXe , ctx.tenXe, ctx.giaXe, min(vp.id.ten) "
					+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp where ";
		
		hql += (isAdmin ? " 1=1" : " isActive = 1 ");

		if(isAdmin || !keyword.equals("") || !vehicleType.equals("All")) {
			hql += " and ";
			hql += (keyword.equals("") ? " 1=1" : " ctx.tenXe like concat('%',:keyword,'%') ");
			hql += " and ";
			hql += (vehicleType.equals("All") ? " 1=1 " : " ctx.loaiXe = :vehicleType ");			
		}
		
		hql += " group by ctx.tenXe, ctx.giaXe, ctx.maXe ";
		switch(sType) {
		case LOW2HIGH:
			hql += " order by ctx.giaXe asc ";
			break;
		case A2Z:
			hql += " order by ctx.tenXe asc ";
			break;
		case Z2A:
			hql += " order by ctx.tenXe desc ";
			break;
		case HIGH2LOW:
		default:
			hql += " order by ctx.giaXe desc ";
		}
		
		
		List<Object[]> list = null;
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			if(!keyword.equals(""))
				q.setParameter("keyword", keyword);
			if(!vehicleType.equals("All"))
				q.setParameter("vehicleType", vehicleType);
			
			q.setFirstResult(itemsPerPage * pageIndex);
			q.setMaxResults(itemsPerPage);
			
			list = (List<Object[]>)q.list(); 
		}
				
		List<Product> products = new ArrayList<Product>();
						
		for(Object[] obj : list) 
		{		
			products.add(new Product(obj[0].toString(), 
									obj[1].toString(), 
									Integer.parseInt(obj[2].toString()), 
									obj[3].toString()));
		}
		return products;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getLoaiXe(){
		String hql = "select distinct loaiXe from ChiTietXe ";
		List<String> list = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			list = (List<String>)q.list();			
			list.add("All");
		}
		return list;
	}
	
	public Product getProduct( String maXe) {
		String hql = "select ctx.tenXe, ctx.giaXe, ctx.description, ctx.loaiXe, ctx.soLuongTonKho, vp.id.ten "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where ctx.maXe = :maXe and vp.id.ten like '%_1.%'";
		Object[] item = null;
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("maXe", maXe);
			item = (Object[])q.uniqueResult();
		}

		return new Product(item[0].toString(), 
						Integer.parseInt(item[1].toString()), 
						item[2].toString(), 
						item[3].toString(), 
						Integer.parseInt(item[4].toString()), 
						item[5].toString(), 
						maXe);
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getRelateProduct( String maXe, String loaiXe){
		String hql = "select ctx.maXe, ctx.tenXe, ctx.giaXe, min(vp.id.ten) "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where ctx.maXe != :maXe and loaiXe = :loaiXe and ctx.isActive = 1"
				+ " group by ctx.maXe, ctx.tenXe, ctx.giaXe";
		
		
		List<Product> products = new ArrayList<Product>();
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("loaiXe", loaiXe);
			q.setParameter("maXe", maXe);
			q.setMaxResults(8);
			List<Object[]> list = (List<Object[]>)q.list();	

			for(Object[] obj : list) {
				Product product = new Product();
				product.setCode(obj[0].toString());
				product.setName(obj[1].toString());
				product.setPrice(Integer.parseInt(obj[2].toString()));
				product.setPicture(obj[3].toString());
				
				products.add(product);
			}
		}
		return products;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getProductPicture( String maXe){
		String hql = "select vp.id.ten "
				+ " from VehiclePictures vp "
				+ " where vp.id.xe.maXe = :maXe order by vp.id.ten asc ";
		List<String> list = null;
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("maXe", maXe);
			q.setMaxResults(4);
			q.setFirstResult(1);
			list = (List<String>)q.list();
		}
		return list;
	}
	
	public int createExportBill(int userId, String paymentMethod) throws Exception{
		try (CSession csession = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = csession.getSession().createStoredProcedureCall("sp_tr_taoDonHang");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(userId);
			pc.registerParameter("hinhThucThanhToan", String.class, ParameterMode.IN).bindValue(paymentMethod);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			return (int)pc.getOutputs().getOutputParameterValue("result");
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public Bill getExportBill(int soHoaDon) throws ParseException {
//		//get export Bill first
//		String hql1 = "select soHoaDon, ngayTao, hinhThucThanhToan "
//				+ " from DonHang "
//				+ " where soHoaDon = :soHoaDon ";
//		
//		//get detail of export Bill
//		String hql2 = "select ctx.tenXe, ctdh.giaXe, ctdh.soLuong, min(vp.id.ten) "
//				+ " from ChiTietDonHang ctdh "
//				+ " inner join ctdh.id.xe ctx "
//				+ " inner join ctx.vehiclePictures vp "
//				+ " where ctdh.id.order.soHoaDon = :soHoaDon "
//				+ " group by ctx.tenXe, ctdh.giaXe, chdh.soLuong ";
//		
//		try(CSession csession = new CSession(sessionFactory.openSession())){
//			Query q1 = csession.getSession().createQuery(hql1);
//			q1.setParameter("orderId", soHoaDon);
//			Object[] objs1 = (Object[])q1.uniqueResult();	
//			
//			Bill exportBill = new Bill();
//			exportBill.setSoHoaDon(Integer.parseInt(objs1[0].toString()));
//			exportBill.setNgayTao(new SimpleDateFormat("yyyy-MM-dd").parse(objs1[1].toString()));
//			exportBill.setHinhThucThanhToan(PaymentMethod.valueOf(objs1[2].toString()));
//
//			
//			Query q2 = csession.getSession().createQuery(hql2);
//			q2.setParameter("orderId", soHoaDon);
//			List<Object[]> objs2 = (List<Object[]>)q2.list();	
//			
//			List<Product> products = new ArrayList<Product>();
//			for(Object[] obj : objs2) {
//				
//				Product product = new Product();
//				product.setName(obj[0].toString());
//				product.setPrice(Integer.parseInt(obj[1].toString()));
//				product.setAmount(Integer.parseInt(obj[2].toString()));
//				product.setPicture(obj[3].toString());
//				products.add(product);
//			}
//			exportBill.setList(products);
//			return exportBill;
//		}		
//	}
	
	@SuppressWarnings("unchecked")
	public List<Bill> getExportBillsBelongToUser(int userId) throws ParseException{
		
		String hql1 = "select soHoaDon, ngayTao, hinhThucThanhToan, trangThaiThanhToan, diaChi, vat"
				+ " from DonHang dh "
				+ " where id.id = :userId "
				+ " order by soHoaDon desc";
		
		String hql2 = "select cthd.id.xe.tenXe, cthd.giaXe, cthd.soLuong, min(vp.id.ten) "
				+ " from ChiTietDonHang cthd "
				+ " inner join cthd.id.xe.vehiclePictures vp "
				+ " where cthd.id.order.soHoaDon = :soHoaDon "
				+ " group by cthd.id.xe.tenXe, cthd.giaXe, cthd.soLuong ";
		
		List<Bill> exportBills = new ArrayList<Bill>();
		try(CSession csession = new CSession(sessionFactory.openSession())){
			Query q1 = csession.getSession().createQuery(hql1);
			Query q2 = csession.getSession().createQuery(hql2);
			
			q1.setParameter("userId", userId);
			List<Object[]> objs1 = (List<Object[]>)q1.list();	
			
			for(Object[] obj1 : objs1) {
				//get export bills that belong to userId
				Bill bill = new Bill();
				
				bill.setSoHoaDon(Integer.parseInt(obj1[0].toString()));
				bill.setNgayTao(new SimpleDateFormat("yyyy-MM-dd").parse(obj1[1].toString()));
				bill.setHinhThucThanhToan(PaymentMethod.valueOf(obj1[2].toString()));
				bill.setTrangThaiThanhToan(Boolean.parseBoolean(obj1[3].toString()));
				bill.setAddress((obj1[4] == null) ? "" : obj1[4].toString());
				bill.setVat(Float.parseFloat(obj1[5].toString()));
				//get detail export bills 
				q2.setParameter("soHoaDon", bill.getSoHoaDon());
				List<Object[]> objs2 = (List<Object[]>)q2.list();	
				
				List<Product> products = new ArrayList<>();
				for(Object[] obj2 : objs2) {
					Product product = new Product();
					
					product.setName(obj2[0].toString());
					product.setPrice(Integer.parseInt(obj2[1].toString()));
					product.setAmount(Integer.parseInt(obj2[2].toString()));
					product.setPicture(obj2[3].toString());
					
					products.add(product);
				}
				
				bill.setList(products);
				exportBills.add(bill);
			}
		}
		return exportBills;
	}
	
	
	public void deleteDonHang(int soHoaDon) {
		String hql1 = "delete from ChiTietDonHang where id.order.soHoaDon  = :soHoaDon";
		String hql2 = "delete from DonHang where soHoaDon  = :soHoaDon";

		Transaction tran = null;
		try (CSession csession = new CSession(sessionFactory.openSession())){
			tran = csession.getSession().beginTransaction();
			Query q1 = csession.getSession().createQuery(hql1);
			q1.setParameter("soHoaDon", soHoaDon);
			q1.executeUpdate();
			
			Query q2 = csession.getSession().createQuery(hql2);
			q2.setParameter("soHoaDon", soHoaDon);
			q2.executeUpdate();
			tran.commit();
		}catch(Exception ex) {
			tran.rollback();
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getIndexCategory() throws Exception{
		String hql = "select ctx.loaiXe, min(vp.id.ten) "
				+ "	from ChiTietXe ctx "
				+ " inner join ctx.vehiclePictures vp "
				+ " where ctx.isActive = 1 "
				+ "	group by ctx.loaiXe ";

		List<Product> products = new ArrayList<Product>();
		try (CSession csession = new CSession(sessionFactory.openSession())){
			 Query q = csession.getSession().createQuery(hql);
			 List<Object[]> list = (List<Object[]>)q.list();
			 
			 for(Object[] obj : list){
				 //get vehicle that is active
				 if(obj != null) {					 
					 Product product = new Product();
					 product.setType(obj[0].toString());
					 product.setPicture(obj[1].toString());
					 products.add(product);
				 }
			 }
		}
		return products;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getTop8BestSellerProduct() throws Exception{
		//get the most buy vehicles in the shop
		String hql1 = "select ctdh.id.xe.maXe, sum(soLuong) as soLuong "
					+ " from ChiTietDonHang ctdh "
					+ " group by ctdh.id.xe.maXe order by soLuong desc ";
		//get information of those best seller vehicles and must be active
		String hql2 = "select ctx.maXe, ctx.tenXe, ctx.giaXe, min(vp.id.ten) "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where ctx.maXe = :maXe and ctx.isActive = 1"
				+ " group by ctx.maXe, ctx.tenXe, ctx.giaXe ";

		List<Product> products = new ArrayList<Product>();
		try (CSession csession = new CSession(sessionFactory.openSession())){
			Query q1 = csession.getSession().createQuery(hql1);
			Query q2 = csession.getSession().createQuery(hql2);
			
			//get only top 8 result in query 
			q1.setMaxResults(8);
			List<Object[]> list = (List<Object[]>)q1.list();
			
			for(Object[] obj : list) {
				q2.setString("maXe", obj[0].toString());
				Object[] obj_ = (Object[])q2.uniqueResult();
				
				//get only vehicles that is active
				if(obj_ != null) {					
					Product product = new Product();
					product.setCode(obj_[0].toString());
					product.setName(obj_[1].toString());
					product.setPrice(Integer.parseInt(obj_[2].toString()));
					product.setPicture(obj_[3].toString());
					products.add(product);
				}
				
			}
			return products;
		}
	}
	
	public Product getSpecialProduct(String maXe) {
		String hql = "select ctx.maXe, ctx.tenXe, ctx.description, min(vp.id.ten) "
				+ "	from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ ((maXe == null) ?  " where ctx.isActive = 1 " : " where ctx.maXe = :maXe ")
				+ "	group by ctx.maXe, ctx.tenXe, ctx.description";
		
		try (CSession csession = new CSession(sessionFactory.openSession())){
			 Query q = csession.getSession().createQuery(hql);
			 
			 if(maXe == null) q.setMaxResults(1);
			 else q.setString("maXe", maXe);
			 
			 Object[] objs = (Object[])q.uniqueResult();
			 
			 Product product = new Product();
			 product.setCode(objs[0].toString());
			 product.setName(objs[1].toString());
			 product.setDescription(objs[2].toString());
			 product.setPicture(objs[3].toString());
			 
			 return product;
		}
	}
	
	public Product getIndexProduct(String maXe) {
		String hql = "	select ctx.maXe, ctx.tenXe, ctx.description, min(vp.id.ten) "
					+ "	from ChiTietXe ctx inner join ctx.vehiclePictures vp "
					+ ((maXe == null) ?  " where ctx.isActive = 1 " : " where ctx.maXe = :maXe ")
					+ "	group by ctx.maXe, ctx.tenXe, ctx.description";
		
		try (CSession csession = new CSession(sessionFactory.openSession())){
			Query q = csession.getSession().createQuery(hql);
			 
			 if(maXe == null) 
				 q.setMaxResults(1);
			 else
				 q.setString("maXe", maXe);
			 
			 Object[] objs = (Object[])q.uniqueResult();
			 
			 Product product = new Product();
			 product.setCode(objs[0].toString());
			 product.setName(objs[1].toString());
			 product.setDescription(objs[2].toString());
			 product.setPicture(objs[3].toString());
			 
			 return product;
		}
	}
	
	public boolean checkDate(int month, int year) {
		
		if(month == 0 || year == 0) return false;
		
		LocalDate current = LocalDate.now();
		LocalDate date = LocalDate.of(year, month, 1);
		
		return (date.isBefore(current) || date.isEqual(current)) ? true : false;
	}
	
	public int changeAccount(AFlag...flags) {
		int flagCombo = 0;
		for(AFlag flag : flags) {
			flagCombo |= flag.getValue();
		}
		return flagCombo ;
	}
}
