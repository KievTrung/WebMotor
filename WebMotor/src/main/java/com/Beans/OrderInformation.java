package com.Beans;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class OrderInformation {
	
	@NotBlank(message="Invalid name")
	@Pattern(message="Invalid name", regexp="^[A-Z\s]+$")
	private String cardName;
	@Pattern(message="Invalid pin code", regexp="^[0-9\s]{16,19}")
	private String cardNumber;
	private int month;
	private int year;
	@Pattern(message="Invalid cvv", regexp="[0-9]{3}")
	private String cvv;
	@NotBlank(message="Invalid address")
	private String address;
	
	public OrderInformation() {
		super();
	}

	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "MetaInformation [cardName=" + cardName + ", cardNumber=" + cardNumber + ", month=" + month + ", year="
				+ year + ", cvv=" + cvv + ", address=" + address + "]";
	}
}
