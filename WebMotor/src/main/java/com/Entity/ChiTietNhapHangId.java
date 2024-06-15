package com.Entity;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Embeddable
public class ChiTietNhapHangId implements Serializable{
	@ManyToOne @JoinColumn(name="maXe", nullable=false, referencedColumnName = "maXe", updatable = false, insertable = false)
	private ChiTietXe xe;
	
	@ManyToOne @JoinColumn(name="soHoaDon", nullable=false, referencedColumnName = "soHoaDon", updatable = false, insertable = false)
	private NhapHang importOrder;

	public ChiTietNhapHangId() {
		super();
	}

	public ChiTietXe getXe() {
		return xe;
	}

	public void setXe(ChiTietXe xe) {
		this.xe = xe;
	}

	public NhapHang getImportOrder() {
		return importOrder;
	}

	public void setImportOrder(NhapHang importOrder) {
		this.importOrder = importOrder;
	}

	@Override
	public String toString() {
		return "ChiTietNhapHangId [xe=" + xe + ", importOrder=" + importOrder + "]";
	}
}
