package com.Entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@SuppressWarnings("serial")
@Embeddable
public class ChiTietDonHangId implements Serializable{
	
	@ManyToOne @JoinColumn(name="maXe", nullable=false, referencedColumnName = "maXe", updatable = false, insertable = false)
	private ChiTietXe xe;
	
	@ManyToOne @JoinColumn(name="soHoaDon", nullable=false, referencedColumnName = "soHoaDon", updatable = false, insertable = false)
	private DonHang order;
	
	public ChiTietDonHangId() {
		super();
	}

	public ChiTietDonHangId(ChiTietXe xe, DonHang order) {
		super();
		this.xe = xe;
		this.order = order;
	}

	public ChiTietXe getXe() {
		return xe;
	}

	public void setXe(ChiTietXe xe) {
		this.xe = xe;
	}

	public DonHang getorder() {
		return order;
	}

	public void setorder(DonHang order) {
		this.order = order;
	}

	

	@Override
	public int hashCode() {
		return Objects.hash(order, xe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietDonHangId other = (ChiTietDonHangId) obj;
		return Objects.equals(order, other.order) && Objects.equals(xe, other.xe);
	}

	@Override
	public String toString() {
		return "ChiTietorderId [xe=" + xe + ", order=" + order + "]";
	}
}
