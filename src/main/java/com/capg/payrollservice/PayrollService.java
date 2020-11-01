package com.capg.payrollservice;

import java.sql.*;
import java.util.Enumeration;

public class PayrollService {

	public static final String URL = "jdbc:mysql://localhost:3306/payroll_service";
	public static final String USER = "root";
	public static final String PASSWORD = "Star@Sun98";
	public static Connection connection = null;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Connection Successful");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			System.out.println(" " + driverClass.getClass());
		}
	}
}
