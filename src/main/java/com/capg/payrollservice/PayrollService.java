package com.capg.payrollservice;

import java.sql.*;
import java.util.Enumeration;

public class PayrollService {

	public static final String URL = "jdbc:mysql://localhost:3306/payroll_service";
	public static final String USER = "root";
	public static final String PASSWORD = "Star@Sun98";

	public static void main(String[] args) {
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded!");
			listDrivers();
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println(connection + " Connection established!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass());
		}
	}
}
