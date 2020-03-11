package com.ezplus.imburst.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyDatabase{
	static MyDatabase me = new MyDatabase();
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	private MyDatabase(){
	}
	
	static MyDatabase getDatabase() {
		return me;
	}
	
	static void initialize() {
		if( conn != null) {
			return;
		}
		try {
			conn = DriverManager.getConnection("");
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static PreparedStatement getPreparedStatement(String format) throws SQLException {
		initialize();
		close();
		return conn.prepareStatement(format);
	}
	
	public static ResultSet lookup(String table) {
		initialize();
		close();
		ResultSet rs;
		try {
			pstmt  = conn.prepareStatement("SELECT * FROM "+ table + ";");
			System.out.println(pstmt);
			rs = pstmt.executeQuery();
			rs.getString(0);
			return rs;
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}

	static boolean executeUpdate(PreparedStatement prep, int n) throws SQLException {
		int r = prep.executeUpdate();
		if (r == n) {
			return true;
		}
		if (r < n) {
			return false;
		}
		if (r > n) {
			//throw DatabaseStateException;
		}
		return false;
	}
	
	static void close() {
		if (pstmt == null) {
			return;
		}
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
		}
	}
	
	protected void finalize() throws Throwable{
		if (conn != null) {
			conn.close();
		}
		super.finalize();
	}
}

