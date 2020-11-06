package com.capg.payrollservice;

import java.sql.*;
import java.util.Enumeration;

public class PayrollService {

	public static final String URL = "jdbc:mysql://localhost:3306/payroll_service";
	public static final String USER = "root";
	public static final String PASSWORD = "Star@Sun98";
	public static Connection connection = null;
	private static int connectionCounter = 0;

	public static Connection getConnection() {
		connectionCounter++;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver successfully loaded");
		
			System.out.println("Processing thread: "+Thread.currentThread().getName()+" Connecting to database with Id: "+connectionCounter);
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Processing thread: "+Thread.currentThread().getName()+" Id: "+connectionCounter+ " Connection is successfull!" + connection);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find driver", e);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		listDrivers();
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
