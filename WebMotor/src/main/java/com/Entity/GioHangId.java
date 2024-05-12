package com.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
@Embeddable
public class GioHangId implements Serializable{
	
	@ManyToOne @JoinColumn(name="id", nullable=false, referencedColumnName = "id", insertable = false, updatable = false)
	private Account user;
	
	@ManyToOne @JoinColumn(name="maXe", nullable=false, referencedColumnName = "maXe", insertable = false, updatable = false)
	private ChiTietXe xe;
	
	public GioHangId() {
		super();
	}

	public GioHangId(Account user, ChiTietXe xe) {
		super();
		this.user = user;
		this.xe = xe;
	}

	public Account getUser() {
		return user;
	}

	public void setUser(Account user) {
		this.user = user;
	}

	public ChiTietXe getXe() {
		return xe;
	}

	public void setXe(ChiTietXe xe) {
		this.xe = xe;
	}

	@Override
	public String toString() {
		return "GioHangId [user=" + user + ", xe=" + xe + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, xe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GioHangId other = (GioHangId) obj;
		return Objects.equals(user, other.user) && Objects.equals(xe, other.xe);
	}
	
}
