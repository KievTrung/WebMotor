package com.Entity;


import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="donHang")
public class DonHang {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="soHoaDon")
	private int soHoaDon;
	
	@Column(name="ngayTao") @Temporal(TemporalType.DATE) 
	private Date ngayTao;
	
	@ManyToOne @JoinColumn(name="id", referencedColumnName = "id", updatable = false, insertable = false)
	private Account id;
	
	@Column(name="HinhThucThanhToan")
	private String hinhThucThanhToan;
	
	@Column(name="trangThaiThanhToan")
	private boolean trangThaiThanhToan;

	@Column(name="additionInfo", length=23)
	private String additionInfo;
	
	@Column(name="diaChi")
	private String diaChi;

	@OneToMany(mappedBy="id.order", cascade= {CascadeType.ALL}, fetch=FetchType.LAZY)
	private List<ChiTietDonHang> chiTietDonHangs;
	
	public Account getId() {
		return id;
	}
	public String getDiaChi() {
		return diaChi;
	}
	
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public List<ChiTietDonHang> getChiTietDonHangs() {
		return chiTietDonHangs;
	}

	public void ListChiTietDonHangs(List<ChiTietDonHang> chiTietDonHangs) {
		this.chiTietDonHangs = chiTietDonHangs;
	}
	
	public String getAdditionInfo() {
		return additionInfo;
	}

	public void setAdditionInfo(String additionInfo) {
		this.additionInfo = additionInfo;
	}

	public int getSoHoaDon() {
		return soHoaDon;
	}

	public void ListSoHoaDon(int soHoaDon) {
		this.soHoaDon = soHoaDon;
	}

	public Date getNgayTao() {
		return ngayTao;
	}

	public void ListNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}

	public String getHinhThucThanhToan() {
		return hinhThucThanhToan;
	}

	public void ListHinhThucThanhToan(String hinhThucThanhToan) {
		this.hinhThucThanhToan = hinhThucThanhToan;
	}

	public boolean isTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void ListTrangThaiThanhToan(boolean trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	@Override
	public String toString() {
		return "DonHang [soHoaDon=" + soHoaDon + ", ngayTao=" + ngayTao + ", id=" + id + ", hinhThucThanhToan="
				+ hinhThucThanhToan + ", trangThaiThanhToan=" + trangThaiThanhToan + "]";
	}

}
