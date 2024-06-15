package com.Entity;

import javax.persistence.*;

@Entity
@Table(name="chiTietNhapHang")
public class ChiTietNhapHang {
	@EmbeddedId
	ChiTietNhapHangId id;
	
	private int soLuong;
	private int giaXe;

	public ChiTietNhapHang() {
		super();
	}

	public int getGiaXe() {
		return giaXe;
	}
	public void setGiaXe(int giaXe) {
		this.giaXe = giaXe;
	}
	public ChiTietNhapHangId getId() {
		return id;
	}

	public void setId(ChiTietNhapHangId id) {
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
		return "ChiTietNhapHang [id=" + id + ", soLuong=" + soLuong + "]";
	}
}
