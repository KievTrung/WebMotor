package com.Entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="nhapHang")
public class NhapHang {
	@Id @GeneratedValue
	private int soHoaDon;
	
	@Temporal(TemporalType.DATE) 
	private Date ngayTao;
	
	@ManyToOne @JoinColumn(name="id", nullable=false, referencedColumnName = "id", insertable = false, updatable = false)
	private Account id;
	
	@OneToMany(mappedBy="id.importOrder", cascade= {CascadeType.ALL}, fetch=FetchType.EAGER)
	private Set<ChiTietNhapHang> chiTietNhapHangs;

	public NhapHang() {
		super();
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

	public Account getid() {
		return id;
	}

	public void setid(Account id) {
		this.id = id;
	}

	public Set<ChiTietNhapHang> getChiTietNhapHangs() {
		return chiTietNhapHangs;
	}

	public void setChiTietNhapHangs(Set<ChiTietNhapHang> chiTietNhapHangs) {
		this.chiTietNhapHangs = chiTietNhapHangs;
	}

	@Override
	public String toString() {
		return "NhapHang [soHoaDon=" + soHoaDon + ", ngayTao=" + ngayTao + ", id=" + id + ", chiTietNhapHangs="
				+ chiTietNhapHangs + "]";
	}
}
