package com.Entity;

import javax.persistence.*;

@Entity
@Table(name="gioHang")
public class GioHang {
	
	@EmbeddedId
	private GioHangId id;
	
	@Column(name="soLuong", nullable=false)
	private int soLuong;
	
	public GioHang() {
		super();
	}
	public GioHangId getId() {
		return id;
	}

	public void setId(GioHangId id) {
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
		return "GioHang [id=(" + id.toString() + "), soLuong=" + soLuong + "]";
	}
}
