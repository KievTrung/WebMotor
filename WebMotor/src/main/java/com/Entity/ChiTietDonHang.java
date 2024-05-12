package com.Entity;

import javax.persistence.*;


@Entity
@Table(name="chiTietDonHang")
public class ChiTietDonHang {
	@EmbeddedId
	private ChiTietDonHangId id;
	
	private int soLuong;
	
	public ChiTietDonHang() {
		super();
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
