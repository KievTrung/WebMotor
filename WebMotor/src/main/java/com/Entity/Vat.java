package com.Entity;

import javax.persistence.*;

@Entity
@Table(name="vat")
public class Vat {
	@Id
	@Column(name="value", nullable=false)
	private float value;
	
	public Vat() {
		super();
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Vat [vat=" + value + "]";
	}
}
