package com.Entity;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="account")
public class Account {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="login_name", unique=true, nullable=false)
	private String loginName;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private int phone;
	
	private String email;
	
	private String diaChi;
	
	@Column(nullable=false)
	private boolean isAdmin;
	
	@Column(nullable=false)
	private boolean isActive;
	
	@OneToMany(mappedBy="id.user", cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	private Set<GioHang> gioHangs;
	
	@OneToMany(mappedBy="id", cascade= {CascadeType.ALL}, fetch=FetchType.LAZY)
	private Set<DonHang> donHangs;
	
	public Account() {
		super();
	}

	public Set<DonHang> getDonHangs() {
		return donHangs;
	}

	public void setDonHangs(Set<DonHang> donHangs) {
		this.donHangs = donHangs;
	}

	public Set<GioHang> getGioHangs() {
		return gioHangs;
	}
	
	public void setGioHangs(Set<GioHang> gioHangs) {
		this.gioHangs = gioHangs;
	}

	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean admin) {
		this.isAdmin = admin;
	}
	public boolean isActive() {
		return isActive;
	}	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", loginName=" + loginName + ", password=" + password + ", phone=" + phone
				+ ", email=" + email + ", diaChi=" + diaChi + ", isAdmin=" + isAdmin + ", isActive=" + isActive;
	}

}
