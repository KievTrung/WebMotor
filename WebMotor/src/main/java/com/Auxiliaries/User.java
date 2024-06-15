package com.Auxiliaries;


import org.hibernate.validator.constraints.*;

public class User {
	private int userId;
	@NotBlank(message="Please fill login name")
	private String username;
	@NotBlank(message="Please fill password")
	private String password;
	@Email(message="Invalid email")
	private String email;
	private String active;
	private String role;
	
	public User() {
		super();
	}
	
	public User(int userId, String username, String active, String role) {
		super();
		this.userId = userId;
		this.username = username;
		this.active = active;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
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
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", active=" + active + ", role=" + role + "]";
	}
	
}
