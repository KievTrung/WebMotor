package com.Auxiliaries;

import java.util.Date;
import java.util.List;

import com.Enums.PaymentMethod;

public class Bill {
	private int soHoaDon;
	private Date ngayTao;
	private String type;
	private int userId;
	private String userName;
	private String address;
	private float vat;
	private PaymentMethod hinhThucThanhToan;
	private boolean trangThaiThanhToan;
	private List<Product> list;
	
	public Bill() {
		super();
	}
	
	
	public Bill(int soHoaDon, Date ngayTao) {
		super();
		this.soHoaDon = soHoaDon;
		this.ngayTao = ngayTao;
	}


	public Bill(int soHoaDon, Date ngayTao, int userId, String userName, String type) {
		super();
		this.soHoaDon = soHoaDon;
		this.ngayTao = ngayTao;
		this.type = type;
		this.userId = userId;
		this.userName = userName;
	}
	public float getVat() {
		return vat;
	}
	public void setVat(float vat) {
		this.vat = vat;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public PaymentMethod getHinhThucThanhToan() {
		return hinhThucThanhToan;
	}
	public void setHinhThucThanhToan(PaymentMethod hinhThucThanhToan) {
		this.hinhThucThanhToan = hinhThucThanhToan;
	}
	public boolean isTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}
	public void setTrangThaiThanhToan(boolean trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}
	public List<Product> getList() {
		return list;
	}
	public void setList(List<Product> list) {
		this.list = list;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getSoHoaDon() {
		return soHoaDon;
	}
	public void setSoHoaDon(int soHoaDon) {
		this.soHoaDon = soHoaDon;
	}
	public Date getNgayTao() {
		return ngayTao;
	}
	public void setNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "Bill [soHoaDon=" + soHoaDon + ", ngayTao=" + ngayTao + ", type=" + type + ", userId=" + userId
				+ ", userName=" + userName + ", address=" + address + ", vat=" + vat + ", hinhThucThanhToan="
				+ hinhThucThanhToan + ", trangThaiThanhToan=" + trangThaiThanhToan + ", list=" + list + "]";
	}

}
