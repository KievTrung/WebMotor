package com.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import java.sql.Statement;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.Auxiliaries.Bill;
import com.Auxiliaries.Product;
import com.Auxiliaries.User;
import com.Entity.ChiTietDonHang;
import com.Entity.ChiTietNhapHang;
import com.Entity.DonHang;
import com.Entity.NhapHang;
import com.Enums.ActiveState;
import com.Enums.PaymentMethod;
import com.Enums.Role;
import com.Enums.SortType;

@Component("AdminUtils")
public class AdminUtils {
	@Autowired
	private SessionFactory sf;
	
	@Autowired
	private JDBC jdbc;
	
	private int imagesUploadedLimit = 6;
	private int itemsPerPage = 10;
	private String uploadImagesPath = "C:\\Users\\Admin\\git\\MyRepo\\WebMotor\\src\\main\\webapp\\WEB-INF\\resources\\upload\\";
	private String vehicleImagesPath = "C:\\Users\\Admin\\git\\MyRepo\\WebMotor\\src\\main\\webapp\\WEB-INF\\resources\\vehicles\\";
	
	public SessionFactory getSessionFactory() {
		return sf;
	}

	public JDBC getJdbc() {
		return jdbc;
	}

	public int getImagesUploadedLimit() {
		return imagesUploadedLimit;
	}

	public void setImagesUploadedLimit(int imagesUploadedLimit) {
		this.imagesUploadedLimit = imagesUploadedLimit;
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public List<Bill> getBillList(int pageIndex, SortType sortType, String billType, String code) throws NumberFormatException, ParseException, SQLException{
		
		String sql = "select soHoaDon, ngayTao, bill.id, login_name, Type from ( ";
		
		switch(billType) {
		case "Export":
			sql += " select soHoaDon, ngayTao, id, 'Export' as Type from donHang) bill";
			break;
		case "Import":
			sql += " select soHoaDon, ngayTao, id, 'Import' as Type from nhapHang) bill";
			break;
		default:
			sql += " select soHoaDon, ngayTao, id, 'Export' as Type from donHang "
				+ " union "
				+ " select soHoaDon, ngayTao, id, 'Import' as Type from nhapHang) bill";
		}
		//get login name
		sql += " inner join (select id, login_name from account) account on bill.id = account.id ";
		
		if(code != null && !code.equals(""))
			sql += " where soHoaDon = " + Integer.parseInt(code);			
		else			
			switch(sortType) {
			case LOW2HIGH:
				sql += " order by ngayTao asc, soHoaDon desc ";
				break;
			case HIGH2LOW: 
			case A2Z: case Z2A:
			default:
				sql += " order by ngayTao desc, soHoaDon desc ";
				break;
			}
				
		List<Bill> bills = new ArrayList<Bill>();
		try(ResultSet rs = jdbc.execSql(sql);){			
			//move to specify row
			while(rs.next()) if(rs.getRow() == (pageIndex * itemsPerPage + 1)) break;
			
			int itemLeft = itemsPerPage;
			do{
				bills.add(new Bill(rs.getInt("soHoaDon"),
						rs.getDate("ngayTao"),
						rs.getInt("id"),
						rs.getString("login_name"),
						rs.getString("Type")));
				--itemLeft;
			}while(rs.next() && itemLeft != 0);
		}
		jdbc.closeConnect();
		return bills;
	}
	
	public long getNumberOfBills(String billType, String code) throws SQLException {		
		String sql = "select count(soHoaDon) as bills from ( ";
		
		switch(billType) {
		case "Export":
			sql += " select soHoaDon from donHang) as bill";
			break;
		case "Import":
			sql += " select soHoaDon from nhapHang) as bill";
			break;
		default:
			sql += " select soHoaDon from donHang "
					+ " union "
					+ " select soHoaDon from nhapHang) as bill";
		}
		
		if(code != null && !code.equals(""))
			sql += " where soHoaDon = " + Integer.parseInt(code);
		
		ResultSet rs = jdbc.execSql(sql);
		rs.next();
		long result = rs.getLong("bills");
		
		jdbc.closeConnect();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUserList(int pageIndex, Role role, ActiveState activeState, String username){
		String hql = "select id, loginName, isActive, isAdmin from Account where ";
		
		switch(role) {
		case ADMIN:
			hql += " isAdmin = true";
			break;
		case USER:
			hql += " isAdmin = false";
			break;
		case BOTH:
		default:
			hql += " 1=1 ";
		}
		hql += " and ";
		switch(activeState) {
		case YES:
			hql += " isActive = true";
			break;
		case NO:
			hql += " isActive = false";
			break;
		case YESNO:
		default:
			hql += " 1=1 ";
		}

		if(username != null && !username.equals(""))
			hql += " and loginName = :username";		
		
		hql += " order by loginName asc";
				
		List<Object[]> list;
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			
			if(username != null && !username.equals(""))
				q.setParameter("username", username);
			
			q.setFirstResult(itemsPerPage * pageIndex);
			q.setMaxResults(itemsPerPage);
			
			list = (List<Object[]>)q.list();
		}
		List<User> users = new ArrayList<User>();
		for(Object[] obj : list) {
			users.add(new User(Integer.parseInt(obj[0].toString()),
								obj[1].toString(),
								obj[2].toString(),
								obj[3].toString()));
		}
		return users;
	}
	
	public long getNumberOfUsers(Role role, ActiveState activeState, String username){		
		String hql = "select count(id) from Account where ";
		
		switch(role) {
		case ADMIN:
			hql += " isAdmin = true";
			break;
		case USER:
			hql += " isAdmin = false";
			break;
		case BOTH:
		default:
			hql += " 1=1 ";
		}
		hql += " and ";
		switch(activeState) {
		case YES:
			hql += " isActive = true";
			break;
		case NO:
			hql += " isActive = false";
			break;
		case YESNO:
		default:
			hql += " 1=1 ";
		}
		
		if(username != null && !username.equals(""))
			hql += " and loginName = :username";	
		
		long result = 0;
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			if(username != null && !username.equals(""))
				q.setParameter("username", username);
			result = (long)q.uniqueResult();
		}
		return result;
	}
	
	public int isInList(String productName, List<Product> list) {
		for(int i=0; i<list.size(); i++) 
		{
			Product productInList = list.get(i);
			if(productName.equals(productInList.getName()) || productName.equals(productInList.getCode())) 
				return i;
		}
		return -1;
	}
	
	public boolean isUploadable(List<String> currentImages, MultipartFile[] newImages) {
		if((currentImages.size() + (newImages[0].isEmpty() ? 0 : newImages.length)) > imagesUploadedLimit) {
			System.out.println("not uploadable");
			return false;
		}
		return true;
	}
	
	public boolean isInDb(String productName) {
		String hql = "select maXe from ChiTietXe where maXe = :maXe or tenXe = :maXe";
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("maXe", productName);
			
			return (q.uniqueResult() == null) ? false : true;
		}
	}
	
	public boolean isImageDeletable(String imageName) {
		//if it not in current folder i.e it in db
		return (new File(uploadImagesPath + imageName).exists()) ? true : false;
	}

	
	public String saveProductToList(Product product, MultipartFile[] newImages, List<Product> list, int index) throws IllegalStateException, IOException {
		String msg = "Edit successfully";
		Product productInList = list.get(index);
		
		List<String> pics = productInList.getPics();
		
		if(isUploadable(pics, newImages))
			product.setPics(insertProductImages(pics, newImages));	
		else {
			msg = "Warning: Upload reach over " + imagesUploadedLimit;
			product.setPics(pics);			
		}
		
		product.setPicture(productInList.getPicture());
		product.setCode(productInList.getCode());
		product.setIsNew(productInList.isNew());
		
		list.set(index, product);
		return msg;
	}

	
	public Product fetchProductInfoFromDb(Product product) {
		String hql = "select ctx.maXe, ctx.tenXe, ctx.giaXe, ctx.loaiXe, ctx.description, ctx.isActive, vp.id.ten "
					+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp "
					+ " where (ctx.maXe = :maXe or ctx.tenXe = :maXe)";
		Object[] obj;
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			
			String productName = product.getName();
			q.setParameter("maXe", (productName == null) ? product.getCode() : productName);
			q.setMaxResults(1);
			obj = (Object[])q.uniqueResult();
			
			if(obj == null) return null;
		}
		product.setName(obj[1].toString());
		product.setPrice(Integer.parseInt(obj[2].toString()));
		product.setType(obj[3].toString());
		product.setDescription(obj[4].toString());
		product.setActive(obj[5].toString());
		product.setPicture(obj[6].toString());		
		product.setCode(obj[0].toString());
		product.setPics(getProductPicture(product.getCode()));
		product.setIsNew(false);
		return product;
	}
	
	public int getProductStocks(String productId) {
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery("select soLuongTonKho from ChiTietXe where maXe = :maXe");
			q.setParameter("maXe", productId);
			return (int)q.uniqueResult();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getProductPicture(String code){
		String hql = "select vp.id.ten from VehiclePictures vp where vp.id.xe.maXe = :maXe";
		List<String> obj;
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("maXe", code);
			obj = (List<String>)q.list();
		}
		List<String> list = new ArrayList<String>();
		for(String name : obj)
			list.add(name);
		return list;
	}
	
	public List<String> insertProductImages(List<String> currentImages, MultipartFile[] newImages) throws IllegalStateException, IOException {
		if(currentImages == null)
			currentImages = new ArrayList<String>(); 
		
		//if images is empty then return;
		if(newImages[0].isEmpty()) return currentImages;
		
		//find the next index to be fetch
		int index = -1;
		
		if(currentImages.size() != 0) {
			//fint the last element in pics
			String name = currentImages.get(currentImages.size() - 1);
			
			//parse name to find the last index
			index = Integer.parseInt(name.substring(name.lastIndexOf("_") + 1, name.lastIndexOf(".")));				
		}
		
		for(MultipartFile image : newImages) {
			// get format of file
			String contentType = image.getContentType();
			String originalFileName = image.getOriginalFilename(); 
			
			String format = contentType.substring(contentType.indexOf("/") + 1, contentType.length());
			String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
			
			//generate new id for the next image to be fetch
			String newId = fileName + "_" + (++index) + "." + format;
			
			//add image to current list
			currentImages.add(newId);
			
			//add image to resources
			image.transferTo(new File(uploadImagesPath + newId));
		}
		return currentImages; 
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCategory(){
		try(CSession csession = new CSession(sf.openSession()))
		{
			Query q = csession.getSession().createQuery("select distinct loaiXe from ChiTietXe ");
			return (List<String>)q.list();
		}	
	}
	
	public boolean deleteImages(List<String> images) {
		for(String image : images) {
			File file = new File(uploadImagesPath + image);
			if(!file.delete())
				return false;
		}
		return true;
	}
	
	public boolean deleteUploadImage(String image) {
		return new File(uploadImagesPath + image).delete();
	}

	public boolean deleteResourcesImage(String image) {
		return new File(vehicleImagesPath+ image).delete();
	}

	public boolean deleteDbImage(String productId, String image) {
		
		try(CSession csession = new CSession(sf.openSession())){
			
			Transaction tran = csession.getSession().beginTransaction();
			try {
				String hql = "delete from VehiclePictures vp where vp.id.xe.maXe = :maXe and vp.id.ten= :ten";
				Query q = csession.getSession().createQuery(hql);
				q.setString("maXe", productId);
				q.setString("ten", image);
				q.executeUpdate();
				tran.commit();
				return true;
			} catch (Exception e) {
				tran.rollback();
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public boolean transferImage(String image, String srcFolder, String destFolder) {
		File file = new File(srcFolder + image);
		if(file.exists()) 
			return file.renameTo(new File(destFolder + image));
		return false;
	}
	
	public String maXeGenerate(Connection connect, String category){
		String maXe = null;
		
		try(PreparedStatement ps = connect.prepareStatement("select top 1 maXe from chiTietXe where loaiXe = ? order by maXe desc")){
			ps.setString(1, category);
			ResultSet rs = ps.executeQuery();
			//move cursor to the the last row
			rs.next();
			//get the last element in list
			maXe = rs.getString("maXe");
		}
		catch(SQLException ex) {
			return maXe = category + "0";
		}
		
		//parsing to get the index of the last element
		//find the first index that can be convert to int
		char[] array = maXe.toCharArray();
		int i=0;
		for(; i<array.length; i++) if(Character.isDigit(array[i])) break;
		//parsing
		int index = Integer.parseInt(maXe.substring(i));
		//create the next maXe id of that category
		return category + (++index);
	}
	
	public int createImport(List<Product> products, int userId) throws SQLException{
		int soHoaDon = -1;
		try (Connection con = jdbc.getConnection()){
			//disable auto commit
			con.setAutoCommit(false);

			String sql1 = "insert into dbo.nhapHang(ngayTao, id) values(?, ?)",
					sql2 = "insert into dbo.chiTietNhapHang(soHoaDon, maXe, soLuong, giaXe) values(?, ?, ?, ?)";
			try(PreparedStatement insertSmt1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement insertSmt2 = con.prepareStatement(sql2);){
				
				//step 1: create import bill
				//create new record in nhapHang table
				insertSmt1.setDate(1, new java.sql.Date(new Date().getTime()));
				insertSmt1.setInt(2, userId);
				insertSmt1.executeUpdate();
				
				//get identity of new record
				try(ResultSet rs = insertSmt1.getGeneratedKeys()){
					rs.next(); 
					soHoaDon = rs.getInt(1);
				}
			
				//step 2: Update information to chiTietXe and vehicle picture from import bill
				for(Product product : products) 
				{
					//if this product is new i.e not in db
					if(product.isNew()) {
						product.setCode(maXeGenerate(con, product.getType()));
						insertNewProduct(con, product);
					}
					else 
						updateProduct(con, product, true);
					
					//create new records in chiTietNhapHang table
					//set soHoaDon
					insertSmt2.setInt(1, soHoaDon);
					//set maXe
					insertSmt2.setString(2, product.getCode());
					//set soLuong
					insertSmt2.setInt(3, product.getAmount());
					//set giaXe
					insertSmt2.setInt(4, product.getPrice());
					//execute query
					insertSmt2.executeUpdate();
				}
				con.commit();
			}
			catch (SQLException e) 
			{
				con.rollback();
				throw e;
			}
		}
		return soHoaDon;
	}
	
	public void insertNewProduct(Connection con, Product newProduct) throws SQLException {
		String sql1 = "insert into dbo.chiTietXe(maXe, tenXe, giaXe, loaiXe, description, soLuongTonKho) values(?, ?, ?, ?, ?, ?)";
		String sql2 = "insert into dbo.vehiclePictures(maXe, ten) values(?, ?)";

		try(PreparedStatement ps1 = con.prepareStatement(sql1);
			PreparedStatement ps2 = con.prepareStatement(sql2);){

			//create new record in chiTietXe
			//set maXe
			String maXe = newProduct.getCode();
			ps1.setString(1, maXe);
			//set tenXe
			ps1.setString(2, newProduct.getName());						
			//set giaXe
			ps1.setInt(3, newProduct.getPrice());						
			//set loaiXe
			ps1.setString(4, newProduct.getType());		
			//set description
			ps1.setString(5, newProduct.getDescription());	
			//set soLuongtonKho
			ps1.setInt(6, newProduct.getAmount());		
			//execute query
			
			ps1.executeUpdate();
			
			for(String image : newProduct.getPics()) {
				//set maXe
				ps2.setString(1, maXe);
				//set image name
				ps2.setString(2, image);
				//execute query
				ps2.executeUpdate();
				//if success then tranfer image to vehicles folder
				transferImage(image, uploadImagesPath, vehicleImagesPath);
			}
		}
	}
	
	public String updateProduct(Connection con, Product product, boolean isAdding) throws SQLException {
		String sql1 = "update chiTietXe "
					+ " set tenXe = ?, giaXe = ?, description = ?, soLuongTonKho = " + (isAdding ? "soLuongTonKho + ?" : "?") 
					+ " where maXe = ?";
		//update new product meta information 
		try(PreparedStatement ps = con.prepareStatement(sql1)){
			ps.setString(1, product.getName());
			ps.setInt(2, product.getPrice());
			ps.setString(3, product.getDescription());
			ps.setInt(4, product.getAmount());
			ps.setString(5, product.getCode());
			ps.executeUpdate();
		}
		//update new fetch images
		try(PreparedStatement ps = con.prepareStatement("insert into vehiclePictures(maXe, ten) values(?,?)")){
			for(String image : product.getPics()) {
				if(transferImage(image, uploadImagesPath, vehicleImagesPath)) {
					ps.setString(1, product.getCode());
					ps.setString(2, image);
					ps.executeUpdate();					
				}
			}
		}
		
		//check if product type has been changed
		//get current loaiXe
		String loaiXe;
		try(PreparedStatement ps = con.prepareStatement("select loaiXe from chiTietXe where maXe = ?")){
			ps.setString(1, product.getCode());
			ResultSet rs = ps.executeQuery();
			rs.next();
			loaiXe = rs.getString("loaiXe");
		}
		
		//if newLoaiXe and loaiXe not equal
		String maXe = product.getCode();
		if(!product.getType().equals(loaiXe)) {
			try(PreparedStatement ps = con.prepareStatement("update chiTietXe set maXe = ?, loaiXe = ? where maXe = ?")){
				maXe = maXeGenerate(con, product.getType());
				ps.setString(1, maXe);
				ps.setString(2, product.getType());
				ps.setString(3, product.getCode());
				ps.executeUpdate();
			}
		}
		return maXe;
	}
	
	public Bill getBill(Integer soHoaDon, String type) {

		String importHql = "from NhapHang where soHoaDon = :soHoaDon",
				exportHql = "from DonHang where soHoaDon = :soHoaDon";
		
		Object obj = null;
		try(CSession csession = new CSession(sf.openSession())){
			
			Query q = csession.getSession().createQuery((type.equals("Import")) ? importHql : exportHql);
			q.setParameter("soHoaDon", soHoaDon);
			obj = (Object)q.uniqueResult();
		}
		
		Bill bill = new Bill();
		bill.setType(type);
		
		if(type.equals("Import")) {
			NhapHang nh = (NhapHang)obj;
			bill.setSoHoaDon(nh.getSoHoaDon());
			bill.setNgayTao(nh.getNgayTao());
			bill.setUserId(nh.getid().getId());
			bill.setUserName(nh.getid().getLoginName());
			bill.setList(getProductsFromChiTietNhapHangs(nh.getChiTietNhapHangs()));
		}
		else {
			DonHang dh = (DonHang)obj;
			bill.setSoHoaDon(dh.getSoHoaDon());
			bill.setNgayTao(dh.getNgayTao());
			bill.setUserId(dh.getId().getId());
			bill.setUserName(dh.getId().getLoginName());
			bill.setVat(dh.getVat());
			bill.setTrangThaiThanhToan(dh.isTrangThaiThanhToan());
			bill.setVat(dh.getVat());
			
			//still need modify
			switch(dh.getHinhThucThanhToan()) {
			case "INPLACE":
				bill.setHinhThucThanhToan(PaymentMethod.INPLACE);
				break;
			case "VISA":
				bill.setHinhThucThanhToan(PaymentMethod.VISA);
				break;
			default:
				bill.setHinhThucThanhToan(PaymentMethod.NA);
			}
			
			bill.setList(getProductsFromChiTietDonHangs(dh.getChiTietDonHangs()));
		}
		return bill;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsFromChiTietNhapHangs(Set<ChiTietNhapHang> set){
		String hql = "select ctx.tenXe, vp.id.ten "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp"
				+ " where ctx.maXe = :maXe";
		
		List<Product> products = new ArrayList<Product>();
		
		try(CSession csession = new CSession(sf.openSession())){
			
			for(ChiTietNhapHang ctnh : set) {
				Query q = csession.getSession().createQuery(hql);
				q.setMaxResults(1);
				q.setParameter("maXe", ctnh.getId().getXe().getMaXe());
				List<Object[]> objs = (List<Object[]>)q.list();
				
				Product product = new Product();
				for(Object[] obj : objs) 
				{
					product.setName(obj[0].toString());
					product.setPicture(obj[1].toString());					
					product.setPrice(ctnh.getGiaXe());
					product.setAmount(ctnh.getSoLuong());
					product.setCode(ctnh.getId().getXe().getMaXe());
				}
				products.add(product);
			}
		}	
		return products;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getProductsFromChiTietDonHangs(Set<ChiTietDonHang> set){
		String hql = "select ctx.tenXe, vp.id.ten "
				+ " from ChiTietXe ctx inner join ctx.vehiclePictures vp"
				+ " where ctx.maXe = :maXe";
		
		List<Product> products = new ArrayList<Product>();
		
		try(CSession csession = new CSession(sf.openSession())){
			
			for(ChiTietDonHang ctnh : set) {
				Query q = csession.getSession().createQuery(hql);
				q.setMaxResults(1);
				q.setParameter("maXe", ctnh.getId().getXe().getMaXe());
				List<Object[]> objs = (List<Object[]>)q.list();
				
				Product product = new Product();
				for(Object[] obj : objs) 
				{
					product.setName(obj[0].toString());
					product.setPicture(obj[1].toString());					
					product.setPrice(ctnh.getGiaXe());
					product.setAmount(ctnh.getSoLuong());
					product.setCode(ctnh.getId().getXe().getMaXe());
				}
				products.add(product);
			}
		}	
		return products;
	}
	
	public List<Bill> getBillsReferenceProduct(String productId) throws SQLException{
		//get bill id that reference product
		String sql = "select soHoaDon, 'Export' as Type, maXe from dbo.chiTietDonHang "
					+ "	union "
					+ "	select soHoaDon, 'Import' as Type, maXe from dbo.chiTietNhapHang";
		List<Bill> list = new ArrayList<Bill>();
		try(Connection connect = jdbc.getConnection()){
			try(PreparedStatement ps = connect.prepareStatement(sql)){
				try(ResultSet rs = ps.executeQuery()){
					//get soHoaDon that is match with productCode
					//then get bill from soHoaDon and its type
					while(rs.next())
						if(rs.getString("maXe").equals(productId))
							list.add(getBill(connect, rs.getInt("soHoaDon"), rs.getString("Type")));
				}
			}
		}
		return list;
	}
	
	public Bill getBill(Connection connect, int soHoaDon, String type) throws SQLException {
		String sql = "select soHoaDon, ngayTao, a.id, login_name ";
		switch(type) {
		case "Export":
			sql += " from DonHang dh inner join (select id, login_name from account) a on dh.id = a.id ";
			break;
		case "Import":
			sql += " from NhapHang nh inner join (select id, login_name from account) a on nh.id = a.id ";
		}
		sql += " where soHoaDon = ?";
		try(PreparedStatement ps = connect.prepareStatement(sql)){
			ps.setInt(1, soHoaDon);
			try(ResultSet rs = ps.executeQuery()){
				rs.next();
				return new Bill(rs.getInt("soHoaDon"),
								rs.getDate("ngayTao"),
								rs.getInt("id"),
								rs.getString("login_name"),
								type);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Bill> getImportExportBillBelongToUser(int userId, boolean isImport) throws NumberFormatException, ParseException{
		
		String hql = "select soHoaDon, ngayTao from " + ((isImport) ? "NhapHang" : "DonHang") +" where id.id = :userId";
		
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			q.setParameter("userId", userId);
			List<Object[]> list = (List<Object[]>)q.list();	
			
			List<Bill> bills = new ArrayList<Bill>();

			for(Object[] obj : list)
				bills.add(new Bill(Integer.parseInt(obj[0].toString()), 
								new SimpleDateFormat("yyyy-MM-dd").parse(obj[1].toString())));
			
			return bills;
		}	
	}
	
	public List<Bill> getExportBillFromStartToEnd(Date start, Date end) throws SQLException {
		String sql = "select soHoaDon, ngayTao, login_name, vat "
				+ " from donHang dh inner join account a on dh.id = a.id "
				+ " where ngayTao between ? and ?"
				+ " order by ngayTao desc";
		
		List<Bill> bills = new ArrayList<Bill>();
		try(Connection connect = jdbc.getConnection()){
			try(PreparedStatement ps = connect.prepareStatement(sql)){
				ps.setDate(1, new java.sql.Date(start.getTime()));
				ps.setDate(2, new java.sql.Date(end.getTime()));
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					Bill bill = new Bill();
					bill.setSoHoaDon(rs.getInt("soHoaDon"));
					bill.setNgayTao(rs.getDate("ngayTao"));
					bill.setUserName(rs.getString("login_name"));
					bill.setVat(rs.getFloat("vat"));
					bills.add(bill);
				}
			}			
		}	
		return bills;
	}

	public List<Bill> getImportBillFromStartToEnd(Date start, Date end) throws SQLException {
		String sql = "select soHoaDon, ngayTao, login_name"
				+ " from nhapHang nh inner join account a on nh.id = a.id "
				+ " where ngayTao between ? and ?"
				+ " order by ngayTao desc";
		
		List<Bill> bills = new ArrayList<Bill>();
		try(Connection connect = jdbc.getConnection()){
			try(PreparedStatement ps = connect.prepareStatement(sql)){
				ps.setDate(1, new java.sql.Date(start.getTime()));
				ps.setDate(2, new java.sql.Date(end.getTime()));
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()) {
					Bill bill = new Bill();
					bill.setSoHoaDon(rs.getInt("soHoaDon"));
					bill.setNgayTao(rs.getDate("ngayTao"));
					bill.setUserName(rs.getString("login_name"));
					bills.add(bill);
				}
			}			
		}	
		return bills;
	}
	
	@SuppressWarnings("unchecked")
	public double caculateTotalExportBills(List<Bill> bills) {
		String hql = "select soLuong, giaXe from ChiTietDonHang where id.order.soHoaDon = :soHoaDon";
		double total = 0;
		
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			
			for(Bill bill : bills) {
				q.setParameter("soHoaDon", bill.getSoHoaDon());
				List<Object[]> list = (List<Object[]>)q.list();	
				double vat = bill.getVat();
				for(Object[] obj : list) {
					double soLuong = Double.parseDouble(obj[0].toString());
					double giaXe = Double.parseDouble(obj[1].toString());

					total +=  soLuong * giaXe  * (1 + vat/100);
				}
			}
		}
		return total;
	}
	
	@SuppressWarnings("unchecked")
	public double caculateTotalImportBills(List<Bill> bills) {
		String hql = "select soLuong, giaXe from ChiTietNhapHang where id.importOrder.soHoaDon = :soHoaDon";
		double total = 0;
		
		try(CSession csession = new CSession(sf.openSession())){
			Query q = csession.getSession().createQuery(hql);
			
			for(Bill bill : bills) {
				q.setParameter("soHoaDon", bill.getSoHoaDon());
				List<Object[]> list = (List<Object[]>)q.list();	
				for(Object[] obj : list) {
					double soLuong = Double.parseDouble(obj[0].toString());
					double giaXe = Double.parseDouble(obj[1].toString());
					total +=  soLuong * giaXe;
				}
			}
		}
		return total;
	}

	public long getUserCount() {
		try(CSession csession = new CSession(sf.openSession())){
			String hql = "select count(id) from Account where isActive = 1 and isAdmin = 0";		
			Query q = csession.getSession().createQuery(hql);
			return (long)q.uniqueResult();			
		}
	}
	
	public long getAdminCount() {
		try(CSession csession = new CSession(sf.openSession())){
			String hql = "select count(id) from Account where isActive = 1 and isAdmin = 1";		
			Query q = csession.getSession().createQuery(hql);
			return (long)q.uniqueResult();	
		}
	}
	
	public float getVat() {
		try(CSession csession = new CSession(sf.openSession())){
			String hql = "select value from Vat";		
			Query q = csession.getSession().createQuery(hql);
			return (float)q.uniqueResult();				
		}
	}

	public void updateVat(double vat) {
		try(CSession csession = new CSession(sf.openSession())){
			Transaction tran = csession.getSession().beginTransaction();
			try {
				String hql = "update Vat set value = :vat";
				Query q = csession.getSession().createQuery(hql);
				q.setDouble("vat", vat);
				q.executeUpdate();
				tran.commit();
			} catch (Exception e) {
				tran.rollback();
				e.printStackTrace();
			}
		}
	}
	
	
}
