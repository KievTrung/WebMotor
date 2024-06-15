package com.Auxiliaries;

import java.util.List;

public class Product {
	
	private String code;
	private String picture = null;
	private List<String>pics;
	private boolean isNew = true;
	
	private String name;
	private Integer price = null;
	private Integer amount;
	private String description;
	private String type;
	private String active;
	
	public Product() {
		super();
	}

	public Product(String type, String picture) {
		super();
		this.picture = picture;
		this.type = type;
	}
	
	public Product(String name, Integer price, String picture) {
		super();
		this.name = name;
		this.price = price;
		this.picture = picture;
	}
	
	public Product(String name, String description, String picture, String code) {
		super();
		this.name = name;
		this.description = description;
		this.picture = picture;
		this.code = code;
	}

	public Product(String code, String name, Integer price, String picture) {
		super();
		this.name = name;
		this.price = price;
		this.picture = picture;
		this.code = code;
	}
	
	
	public Product(String name, Integer price, Integer amount, String picture) {
		super();
		this.name = name;
		this.price = price;
		this.picture = picture;
		this.amount = amount;
	}

	public Product(String code, String name, Integer price, Integer amount, String picture) {
		super();
		this.code = code;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.picture = picture;
	}
	
	public Product(String name, Integer price, String description, String type, Integer amount, String picture,  String code) {
		super();
		this.name = name;
		this.price = price;
		this.picture = picture;
		this.description = description;
		this.type = type;
		this.amount = amount;
		this.code = code;
	}
	
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [code=" + code + ", picture=" + picture + ", pics=" + pics + ", isNew=" + isNew + ", name="
				+ name + ", price=" + price + ", amount=" + amount + ", description=" + description + ", type=" + type
				+ ", active=" + active + "]";
	}
}
