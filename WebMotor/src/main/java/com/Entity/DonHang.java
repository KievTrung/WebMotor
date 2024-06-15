package com.Entity;


import java.util.Date;
import java.util.Set;

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
	
	private float vat;

	@OneToMany(mappedBy="id.order", cascade= {CascadeType.ALL}, fetch=FetchType.EAGER)
	private Set<ChiTietDonHang> chiTietDonHangs;
	
	public Account getId() {
		return id;
	}
	
	public float getVat() {
		return vat;
	}

	public void setVat(float vat) {
		this.vat = vat;
	}

	public String getDiaChi() {
		return diaChi;
	}
	
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public Set<ChiTietDonHang> getChiTietDonHangs() {
		return chiTietDonHangs;
	}

	public void SetChiTietDonHangs(Set<ChiTietDonHang> chiTietDonHangs) {
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

	public void SetSoHoaDon(int soHoaDon) {
		this.soHoaDon = soHoaDon;
	}

	public Date getNgayTao() {
		return ngayTao;
	}

	public void SetNgayTao(Date ngayTao) {
		this.ngayTao = ngayTao;
	}

	public String getHinhThucThanhToan() {
		return hinhThucThanhToan;
	}

	public void SetHinhThucThanhToan(String hinhThucThanhToan) {
		this.hinhThucThanhToan = hinhThucThanhToan;
	}

	public boolean isTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void SetTrangThaiThanhToan(boolean trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	@Override
	public String toString() {
		return "DonHang [soHoaDon=" + soHoaDon + ", ngayTao=" + ngayTao + ", id=" + id + ", hinhThucThanhToan="
				+ hinhThucThanhToan + ", trangThaiThanhToan=" + trangThaiThanhToan + ", additionInfo=" + additionInfo
				+ ", diaChi=" + diaChi + ", vat=" + vat + ", chiTietDonHangs=" + chiTietDonHangs + "]";
	}

}
