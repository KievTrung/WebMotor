package com.Entity;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table
public class ChiTietXe {
	@Id @Column(nullable=false, length=10)
	private String maXe;
	
	@Column(unique=true, nullable=false)
	private String tenXe;
	
	@Column(nullable=false)
	private long giaXe;
	
	@Column(nullable=false)
	private String loaiXe;
	
	@Column(columnDefinition="nvarchar(max) not null")
	private String description;
	
	@Column(nullable=false)
	private int soLuongTonKho;
	
	private int indexMode;
	
	@OneToMany(mappedBy="id.xe", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	private Set<GioHang> gioHangs;
	
	@OneToMany(mappedBy="id.xe", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	private Set<ChiTietDonHang> chiTietDonHangs;
	
	@OneToMany(mappedBy="id.xe", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
	private Set<VehiclePictures> vehiclePictures;
	
	public ChiTietXe() {
		super();
	}
	
	public int getIndexMode() {
		return indexMode;
	}

	public void setIndexMode(int indexMode) {
		this.indexMode = indexMode;
	}

	public Set<VehiclePictures> getVehiclePictures() {
		return vehiclePictures;
	}

	public void setVehiclePictures(Set<VehiclePictures> vehiclePictures) {
		this.vehiclePictures = vehiclePictures;
	}

	public Set<ChiTietDonHang> getChiTietDonHangs() {
		return chiTietDonHangs;
	}

	public void setChiTietDonHangs(Set<ChiTietDonHang> chiTietDonHangs) {
		this.chiTietDonHangs = chiTietDonHangs;
	}

	public Set<GioHang> getGioHangs() {
		return gioHangs;
	}

	public void setGioHangs(Set<GioHang> gioHangs) {
		this.gioHangs = gioHangs;
	}

	public String getMaXe() {
		return maXe;
	}

	public void setMaXe(String maXe) {
		this.maXe = maXe;
	}

	public String getTenXe() {
		return tenXe;
	}

	public void setTenXe(String tenXe) {
		this.tenXe = tenXe;
	}

	public long getGiaXe() {
		return giaXe;
	}

	public void setGiaXe(long giaXe) {
		this.giaXe = giaXe;
	}

	public String getLoaiXe() {
		return loaiXe;
	}

	public void setLoaiXe(String loaiXe) {
		this.loaiXe = loaiXe;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSoLuongTonKho() {
		return soLuongTonKho;
	}

	public void setSoLuongTonKho(int soLuongTonKho) {
		this.soLuongTonKho = soLuongTonKho;
	}

	@Override
	public String toString() {
		return "ChiTietXe [tenXe=" + tenXe + ", giaXe=" + giaXe + "]";
	}
	
}
