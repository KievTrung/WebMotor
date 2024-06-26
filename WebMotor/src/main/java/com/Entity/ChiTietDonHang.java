package com.Entity;

import javax.persistence.*;


@Entity
@Table(name="chiTietDonHang")
public class ChiTietDonHang {
	@EmbeddedId
	private ChiTietDonHangId id;
	
	private int soLuong;
	private int giaXe;
	
	public ChiTietDonHang() {
		super();
	}
	
	public int getGiaXe() {
		return giaXe;
	}

	public void setGiaXe(int giaXe) {
		this.giaXe = giaXe;
	}

	public ChiTietDonHangId getId() {
		return id;
	}

	public void setId(ChiTietDonHangId id) {
		this.id = id;
	}
	
	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	@Override
	public String toString() {
		return "ChiTietDonHang [id=" + id + ", soLuong=" + soLuong + "]";
	}

}
