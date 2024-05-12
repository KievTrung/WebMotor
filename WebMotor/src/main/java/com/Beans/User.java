package com.Beans;


import org.hibernate.validator.constraints.*;

public class User {
	@NotBlank(message="Please fill login name")
	private String username;
	
	@NotBlank(message="Please fill password")
	private String password;
	
	@Email(message="Invalid email")
	private String email;
	
	public User() {
		super();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + "]";
	}
}
