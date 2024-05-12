package com.Beans;

import java.util.Date;
import java.util.List;

import com.Enums.PaymentMethod;

public class Order {
	private int orderId;
	private Date date;
	private PaymentMethod paymentMethod;
	private boolean trangThaiThanhToan;
	private String address;
	private List<Product> list;
	
	public Order() {
		super();
	}

	public Order(int orderId, Date date, String paymentMethod) {
		super();
		this.orderId = orderId;
		this.date = date;
		this.paymentMethod = PaymentMethod.valueOf(paymentMethod);
	}

	public Order(int orderId, Date date, String paymentMethod, boolean trangThaiThanhToan, String address) {
		super();
		this.orderId = orderId;
		this.date = date;
		this.trangThaiThanhToan = trangThaiThanhToan;
		this.paymentMethod = PaymentMethod.valueOf(paymentMethod);
	}
	
	public boolean getTrangThaiThanhToan() {
		return trangThaiThanhToan;
	}

	public void setTrangThaiThanhToan(boolean trangThaiThanhToan) {
		this.trangThaiThanhToan = trangThaiThanhToan;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<Product> getList() {
		return list;
	}

	public void setList(List<Product> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", date=" + date + ", paymentMethod=" + paymentMethod + ", list=" + list
				+ "]";
	}
}
