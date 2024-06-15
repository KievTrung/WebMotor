package com.Utils;

import java.sql.*;

public class JDBC {
	private String url;
	private String username;
	private String password;
	private Connection connect = null;
	
	public JDBC(String url, String username, String password) {
		super();
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public ResultSet execSql(String sql) throws SQLException{
		
		connect = DriverManager.getConnection(url, username, password);
		Statement s = connect.createStatement();
		return s.executeQuery(sql);
	}
	
	public void closeConnect() throws SQLException {
		if(connect != null)
			connect.close();
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
}
