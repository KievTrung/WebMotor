package com.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ParameterMode;
import javax.servlet.http.HttpSession;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Beans.Order;
import com.Beans.OrderInformation;
import com.Beans.Product;
import com.Entity.Account;
import com.Enums.IndexMode;
import com.Enums.PaymentMethod;
import com.Enums.SortType;
import com.Enums.VehicleType;

@Component("userUtils")
public class UserUtils {
	@Autowired
	SessionFactory sessionFactory;
	
	private String email = "n21dccn004@student.ptithcm.edu.vn";
	private double vat = 0.1;
	
	public UserUtils() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getVat() {
		return vat;
	}

	public void setVat(double vat) {
		this.vat = vat;
	}

	public int datHang( int id, String hinhThucThanhToan) {
		int result = 0;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_datHang");
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
		
		//update order
		String hql = " update DonHang "
				+ " set additionInfo = :ai, hinhThucThanhToan = :pm, diaChi = :addr, trangThaiThanhToan = 1 "
				+ " where soHoaDon = :orderId";
		
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			
			tran = session.getSession().beginTransaction();
			
			Query q = session.getSession().createQuery(hql);
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_updateStock");
			pc.registerParameter("soHoaDon", int.class, ParameterMode.IN).bindValue(soHoaDon);
			pc.getOutputs();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
		
	public void restoreDonHang(int soHoaDon) throws Exception{
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_restoreDonHang");
			pc.registerParameter("soHoaDon", int.class, ParameterMode.IN).bindValue(soHoaDon);
			pc.getOutputs();
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getCart( int id) {
		String hql = "select gh.id.xe.maXe, gh.id.xe.tenXe, gh.id.xe.giaXe, gh.soLuong, vp.id.ten"
				+ " from GioHang gh inner join gh.id.xe.vehiclePictures vp"
				+ " where gh.id.user.id = :id and vp.id.ten like '%_0.%'";
		List<Object[]> list = null; 
		try (CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("id", id);
			list = (List<Object[]>)q.list();			
		}
		List<Product> cart = new ArrayList<Product>();
		for(Object[] obj : list) {
			Product product = new Product(obj[0].toString(),
										obj[1].toString(), 
										Integer.parseInt(obj[2].toString()),
										Integer.parseInt(obj[3].toString()),
										obj[4].toString());
			cart.add(product);
		}
		return cart;
	}
	
	public int setUpTaiKhoan( int flag, String login, String password, int phone, String email, String diaChi, boolean isAdmin, boolean isActive) {
		int result = 0;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_setUpTaiKhoan");
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
	
	public int setUpXe( int flag, String maXe, String tenXe, int giaXe, String loaiXe, String description, int soLuongTonKho, int indexMode) {
		int result = 0;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_setUpXe");
			pc.registerParameter("maXe", String.class, ParameterMode.IN).bindValue(maXe);
			pc.registerParameter("tenXe", String.class, ParameterMode.IN).bindValue(tenXe);
			pc.registerParameter("loaiXe", String.class, ParameterMode.IN).bindValue(loaiXe);
			pc.registerParameter("giaXe", int.class, ParameterMode.IN).bindValue(giaXe);
			pc.registerParameter("descrip", String.class, ParameterMode.IN).bindValue(description);
			pc.registerParameter("soLuongTonKho", int.class, ParameterMode.IN).bindValue(soLuongTonKho);
			pc.registerParameter("indexMode", int.class, ParameterMode.IN).bindValue(indexMode);
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_themGioHang");
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("id", id);
			result = (Long)q.uniqueResult();
		}
		return (result == null ? 0 : result);
	}
	
	public void removeCartItems( String productId, int userId) {
		String hql = "delete from GioHang where id.xe.maXe = :productId and id.user.id = :userId";
		Transaction tran = null;
		try (CSession session = new CSession(sessionFactory.openSession())){
			tran = session.getSession().beginTransaction();
			Query q = session.getSession().createQuery(hql);
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			tran = session.getSession().beginTransaction();
			Query q = session.getSession().createQuery(hql);
			q.setParameter("userId", userId);
		 	System.out.println("testing :" + q.executeUpdate());
			tran.commit();
		}catch(Exception ex) {
			tran.rollback();
			ex.printStackTrace();
		}
	}
	
	public int changeItemAmountInCart( int userId, String maXe, int soLuong) {
		int result = 0;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_thayDoiSoLuongItemTrongGioHang");
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
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_addVehiclePicture");
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
	
	public int changeLogin( int id, String newLogin) {
		int result = 0;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_changeLoginName");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(id);
			pc.registerParameter("newLogin", String.class, ParameterMode.IN).bindValue(newLogin);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public Account getUser( String login) {
		String hql = "from Account where loginName = :login";
		
		Account user = null;
		
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("login", login);
			user = (Account)q.uniqueResult();				
		}
		return user;
	}
	
	public Account checkUser( String login, String password) {
		
		String hql = "from Account where loginName = :login and password = :pass";
		Account user = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("login", login);
			q.setParameter("pass", password);
			user = (Account)q.uniqueResult();
		}
		return user;
	}
		
	public Integer checkLogin( String login) {
		String hql = "select id from Account where loginName = :login";
		Integer id = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("login", login);
			id = (Integer)q.uniqueResult();
		}
		
		return id;
	}
	
	public String checkEmail( String email) {
		String hql = "select loginName from Account where email = :email";
		
		String login = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("email", email);
			login = (String)q.uniqueResult();
		}
		
		return login;
	}
	
	public Integer checkPhone( int phone) {
		String hql = "select id from Account where phone = :phone";
		
		Integer id = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("phone", phone);
			id = (Integer)q.uniqueResult();
		}
		
		return id;
	}
	
	private int numberOfColumn = 4;
	private int itemsPerPage = 8;

	public long getNumberOfPages(HttpSession s,  String vehicleType, String keyword) {
		
		String hql = "select count(maXe) from ChiTietXe " 
		+ ((keyword.equals("")) ? keyword : "where tenXe like concat('%',:keyword,'%') ") 
		+ (vehicleType.equals("All") ? "" : ((keyword.equals("")) ? "where" : "and") + " loaiXe = :vehicleType");
	
		long result = 0;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			if(!keyword.equals(""))
				q.setParameter("keyword", keyword);
			if(!vehicleType.equals("All"))
				q.setParameter("vehicleType", vehicleType);
			result = (long)q.uniqueResult();
		}
		
		s.setAttribute("numberOfRecords", result);
		
		double pages = result / (double)itemsPerPage;
		
		return (pages - (long)pages) > 0 ? (long)(++pages) : ((long)pages == 0 ? (long)(++pages) : (long)pages);
	}
	
	@SuppressWarnings("unchecked")
	public List<List<Product>> getChiTietXeList( int pageIndex, SortType sType, String vehicleType, String keyword) {
		String sortType = "order by ";
		switch(sType) {
		case LOW2HIGH:
			sortType += "ctx.giaXe asc";
			break;
		case A2Z:
			sortType += "ctx.tenXe asc";
			break;
		case Z2A:
			sortType += "ctx.tenXe desc";
			break;
		case HIGH2LOW:
		default:
			sortType += "ctx.giaXe desc";
		}
		
		String hql = "select ctx.tenXe, ctx.giaXe, vp.id.ten, ctx.maXe "
					+ "from ChiTietXe ctx inner join ctx.vehiclePictures vp "
					+ "where (vp.id.ten like '%_0.%' or vp.id.ten like '%_0.%') "
					+ ((keyword.equals("")) ? keyword : " and ctx.tenXe like concat('%',:keyword,'%') ")
					+ (vehicleType.equals("All") ? "" : " and ctx.loaiXe = :vehicleType ")
					+ sortType;
		
		List<Object[]> list = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			if(!keyword.equals(""))
				q.setParameter("keyword", keyword);
			if(!vehicleType.equals("All"))
				q.setParameter("vehicleType", vehicleType);
			
			q.setFirstResult(itemsPerPage * pageIndex);
			q.setMaxResults(itemsPerPage);
			
			list = (List<Object[]>)q.list(); 
		}
		
		List<List<Product>> page = new ArrayList<List<Product>>();
		
		List<Product> products = new ArrayList<Product>();
				
		int i=1, size = list.size();
		
		for(Object[] obj : list) 
		{		
			products.add(new Product(obj[0].toString(), Integer.parseInt(obj[1].toString()), obj[2].toString(), obj[3].toString()));
			i++;
			size--;
				
			if(size == 0) 
			{
				page.add(products);
				break;
			}
			else if(i > numberOfColumn) 
			{
				page.add(products);
				products = new ArrayList<Product>();
				i = 1;
			}
		}
		return page;
	}
	
	public List<String> getLoaiXe(){
		String hql = "select distinct loaiXe from ChiTietXe ";
		List<String> list = null;
		try (CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
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
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
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
	public List<Product> getRelateProduct( String maXeExclude, String loaiXe){
		String hql = "select ctx.tenXe, ctx.giaXe, vp.id.ten, ctx.maXe"
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where ctx.loaiXe = :loaiXe and ctx.maXe != :maXeExclude "
				+ " and vp.id.ten like '%_0.%'";
		
		List<Object[]> arr = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("loaiXe", loaiXe);
			q.setParameter("maXeExclude", maXeExclude);
			q.setMaxResults(4);
			arr = (List<Object[]>)q.list();	
		}
		
		List<Product> list = new ArrayList<Product>();
		for(Object[] obj : arr)
			list.add(new Product(obj[0].toString(), Integer.parseInt(obj[1].toString()), obj[2].toString(), obj[3].toString()));
			
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getProductPicture( String maXe){
		String hql = "select vp.id.ten "
				+ " from VehiclePictures vp "
				+ " where vp.id.xe.maXe = :maXe order by vp.id.ten asc ";
		List<String> list = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("maXe", maXe);
			q.setMaxResults(4);
			q.setFirstResult(1);
			list = (List<String>)q.list();
		}
		return list;
	}
	
	public int taoDonHang( int userId, String paymentMethod) {
		int result = -1;
		try (CSession session = new CSession(sessionFactory.openSession())){
			ProcedureCall pc = session.getSession().createStoredProcedureCall("sp_tr_taoDonHang");
			pc.registerParameter("id", int.class, ParameterMode.IN).bindValue(userId);
			pc.registerParameter("hinhThucThanhToan", String.class, ParameterMode.IN).bindValue(paymentMethod);
			pc.registerParameter("result", int.class, ParameterMode.OUT);
			result = (int)pc.getOutputs().getOutputParameterValue("result");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Order getOrder( int orderId) {
		String hql = "select dh.soHoaDon, dh.ngayTao, dh.hinhThucThanhToan, ctx.tenXe, ctx.giaXe, ctdh.soLuong, vp.id.ten "
				+ " from DonHang dh "
				+ " inner join dh.chiTietDonHangs ctdh "
				+ "	inner join ctdh.id.xe ctx "
				+ " inner join ctx.vehiclePictures vp "
				+ " where dh.soHoaDon = :orderId and vp.id.ten like '%_0.%'";
		
		List<Object[]> list = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("orderId", orderId);
			list = (List<Object[]>)q.list();	
		}
		
		//get order info
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Object[] metaInfo = list.get(0);

		Order order;
		try {
			order = new Order(Integer.parseInt(metaInfo[0].toString()), 
									formatter.parse(metaInfo[1].toString()), 
									metaInfo[2].toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		//get detail order info
		List<Product> products = new ArrayList<Product>();
		for(Object[] obj : list) 
			products.add(new Product(obj[3].toString(), Integer.parseInt(obj[4].toString()), Integer.parseInt(obj[5].toString()), obj[6].toString()));
		
		order.setList(products);
		return order;
	}
	
	@SuppressWarnings("unchecked")
	public List<Order> getListOrder( int userId){
		
		String hql = "select dh.soHoaDon, dh.ngayTao, dh.hinhThucThanhToan, dh.trangThaiThanhToan, dh.diaChi"
				+ " from DonHang dh "
				+ " where dh.id.id = :userId";
			
		List<Object[]> list = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("userId", userId);
			list = (List<Object[]>)q.list();	
		}
		
		List<Order> orders = new ArrayList<Order>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			for(Object[] obj : list) {
				int orderId = Integer.parseInt(obj[0].toString());
				Date date = formatter.parse(obj[1].toString());
				String paymentMethod = obj[2].toString();
				boolean state = Boolean.parseBoolean(obj[3].toString()); 
				String address = (obj[4] == null) ? "" : obj[4].toString();
				
				Order order = new Order(orderId, date, paymentMethod, state, address);
				order.setList(getProductList( orderId));
				orders.add(order);
			}
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return orders;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductList( int orderId){
		String hql = "select cthd.id.xe.tenXe, cthd.id.xe.giaXe, cthd.soLuong, vp.id.ten"
				+ " from ChiTietDonHang cthd inner join cthd.id.xe.vehiclePictures vp"
				+ " where cthd.id.order.soHoaDon = :orderId and vp.id.ten like '%_0.%'";
		
		List<Object[]> list = null;
		try(CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("orderId", orderId);
			list = (List<Object[]>)q.list();	
		}
		
		List<Product> products = new ArrayList<>();
		for(Object[] obj : list) {
			Product product = new Product(obj[0].toString(),
										Integer.parseInt(obj[1].toString()),
										Integer.parseInt(obj[2].toString()),
										obj[3].toString());
			products.add(product);
		}
		return products;
	}
	
	public void deleteDonHang(int soHoaDon) {
		String hql1 = "delete from ChiTietDonHang where id.order.soHoaDon  = :soHoaDon";
		String hql2 = "delete from DonHang where soHoaDon  = :soHoaDon";

		Transaction tran = null;
		try (CSession session = new CSession(sessionFactory.openSession())){
			tran = session.getSession().beginTransaction();
			Query q1 = session.getSession().createQuery(hql1);
			q1.setParameter("soHoaDon", soHoaDon);
			q1.executeUpdate();
			
			Query q2 = session.getSession().createQuery(hql2);
			q2.setParameter("soHoaDon", soHoaDon);
			q2.executeUpdate();
			tran.commit();
		}catch(Exception ex) {
			tran.rollback();
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<List<Product>> getCategoryPicture() throws Exception{
		String hql = "select ctx.loaiXe, vp.id.ten "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where (ctx.indexMode = :indexMode1 or ctx.indexMode = :indexMode2) "
				+ " and vp.id.ten like '%_0.%'";
		List<Object[]> list = null;
		try (CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("indexMode1", IndexMode.BOTH.getValue()); 
			q.setParameter("indexMode2", IndexMode.CATEGORY.getValue());
			list = (List<Object[]>)q.list();
		}
		
		List<List<Product>> page = new ArrayList<List<Product>>();
		List<Product> products = new ArrayList<Product>();
		
		int i=1, size = list.size();
		
		for(Object[] obj : list) 
		{		
			products.add(new Product(obj[0].toString(), obj[1].toString()));
			i++;
			size--;
				
			if(size == 0) 
			{
				page.add(products);
				break;
			}
			else if(i > numberOfColumn) 
			{
				page.add(products);
				products = new ArrayList<Product>();
				i = 1;
			}
		}
		
		return page;
	}
	
	@SuppressWarnings("unchecked")
	public List<List<Product>> getFeatureProduct() throws Exception{
		String hql = "select ctx.tenXe, ctx.giaXe, vp.id.ten, ctx.maXe"
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
				+ " where (ctx.indexMode = :indexMode1 or ctx.indexMode = :indexMode2) "
				+ " and vp.id.ten like '%_0.%'";
		
		List<Object[]> list = null;
		try (CSession session = new CSession(sessionFactory.openSession())){
			Query q = session.getSession().createQuery(hql);
			q.setParameter("indexMode1", IndexMode.BOTH.getValue()); 
			q.setParameter("indexMode2", IndexMode.FEATURE.getValue());
			list = (List<Object[]>)q.list();			
		}

		List<List<Product>> page = new ArrayList<List<Product>>();
		List<Product> products = new ArrayList<Product>();
		
		int i=1, size = list.size();
		
		for(Object[] obj : list) 
		{		
			products.add(new Product(obj[0].toString(), 
									Integer.parseInt(obj[1].toString()),
									obj[2].toString(),
									obj[3].toString()));
			i++;
			size--;
				
			if(size == 0) 
			{
				page.add(products);
				break;
			}
			else if(i > numberOfColumn) 
			{
				page.add(products);
				products = new ArrayList<Product>();
				i = 1;
			}
		}
		return page;
	}
}
