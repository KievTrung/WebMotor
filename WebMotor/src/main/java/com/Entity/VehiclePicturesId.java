package com.Entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@SuppressWarnings("serial")
@Embeddable
public class VehiclePicturesId implements Serializable{
	@ManyToOne @JoinColumn(name="maXe", referencedColumnName = "maXe", nullable = false, updatable = false, insertable = false)
	private ChiTietXe xe;
	
	@Column(nullable = false)
	private String ten;
	
	public VehiclePicturesId() {
		super();
	}

	public VehiclePicturesId(ChiTietXe xe, String ten) {
		super();
		this.xe = xe;
		this.ten = ten;
	}

	public ChiTietXe getXe() {
		return xe;
	}

	public void setXe(ChiTietXe xe) {
		this.xe = xe;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String name) {
		this.ten = name;
	}

	@Override
	public String toString() {
		return "VehiclePicturesId [xe=" + xe + ", name=" + ten + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(ten, xe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehiclePicturesId other = (VehiclePicturesId) obj;
		return Objects.equals(ten, other.ten) && Objects.equals(xe, other.xe);
	}
}
