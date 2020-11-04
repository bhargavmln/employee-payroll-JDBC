package com.capg.payrollservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayrollServiceDB {
	EmployeePayrollData empDataObj = null;

	public List<EmployeePayrollData> viewEmployeePayroll() throws DBServiceException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		String query = "select * from Employee_Payroll";
		try (Connection con = new PayrollService().getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender, salary, start);
				employeePayrollList.add(empDataObj);

			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}

	public List<EmployeePayrollData> viewEmployeePayrollByName(String name) throws DBServiceException {
		List<EmployeePayrollData> employeePayrollListByName = new ArrayList<>();
		String query = "select * from Employee_Payroll where name = ?";
		try (Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt(1);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender, salary, start);
				employeePayrollListByName.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByName;
	}

	public void updateEmployeeSalary(String name, double salary) throws DBServiceException {
		String query = String.format("update Employee_Payroll set salary = %.2f where name = '%s';", salary, name);
		try (Connection con = new PayrollService().getConnection()) {
			Statement statement = con.createStatement();
			int result = statement.executeUpdate(query);
			empDataObj = getEmployeePayrollData(name);
			if (result > 0 && empDataObj != null)
				empDataObj.setSalary(salary);
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	public void updateEmployeeSalaryUsingPreparedStatement(String name, double salary) throws DBServiceException {
		String query = "update Employee_Payroll set salary = ? where name = ?";
		try (Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int result = preparedStatement.executeUpdate();
			empDataObj = getEmployeePayrollData(name);
			if (result > 0 && empDataObj != null)
				empDataObj.setSalary(salary);
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	public EmployeePayrollData getEmployeePayrollData(String name) throws DBServiceException {
		return viewEmployeePayroll().stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
	}

	public boolean isEmpPayrollSyncedWithDB(String name) throws DBServiceException {
		try {
			return viewEmployeePayrollByName(name).get(0).equals(getEmployeePayrollData(name));
		} catch (IndexOutOfBoundsException e) {
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return false;
	}

	public List<EmployeePayrollData> viewEmployeePayrollByJoinDateRange(LocalDate startDate, LocalDate endDate)
			throws DBServiceException {
		List<EmployeePayrollData> employeePayrollListByStartDate = new ArrayList<>();
		String query = "select * from Employee_Payroll where start between ? and  ?";
		try (Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender, salary, start);
				employeePayrollListByStartDate.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByStartDate;
	}

	public Map<String, Double> viewEmployeeDataGroupedByGender(String column, String operation)
			throws DBServiceException {
		Map<String, Double> empDataByGender = new HashMap<>();
		String query = String.format("select gender , %s(%s) from Employee_Payroll group by gender;", operation,
				column);
		try (Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				empDataByGender.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return empDataByGender;
	}

	public List<EmployeePayrollData> insertNewEmployeeToDB(String name, String gender, double salary,
			LocalDate start_date) throws DBServiceException {
		Connection con = null;
		int empId = -1;
		try {
			con = PayrollService.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String query = String.format(
				"insert into Employee_Payroll(name , gender, salary , start)" + "values ('%s','%s','%s','%s');",
				name, gender, salary, Date.valueOf(start_date));
		try (Statement statement = con.createStatement()) {

			int rowAffected = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					empId = resultSet.getInt(1);
				empDataObj = new EmployeePayrollData(name, gender, salary, start_date);
				viewEmployeePayroll().add(empDataObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
				return viewEmployeePayroll();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try (Statement statement = con.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = taxablePay = tax;
			String query1 = String.format(
					"insert into payroll_details(id,basic_pay,deductions,taxable_pay,tax,net_pay)"
							+ " values ('%s','%s','%s','%s','%s','%s');",
					empId, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(query1);
			if (rowAffected == 1) {
				empDataObj = new EmployeePayrollData(empId, name, gender, salary, start_date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return viewEmployeePayroll();
	}
}