package com.capg.payrollservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollServiceDB {
	EmployeePayrollData empDataObj = null;
	
	public List<EmployeePayrollData> viewEmployeePayroll() throws DBServiceException
	{
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		String query = "select * from Employee_Payroll";
		try(Connection con = new PayrollService().getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender ,salary,start);
				employeePayrollList.add(empDataObj);	
				
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}
	
	public List<EmployeePayrollData> viewEmployeePayrollByName(String name) throws DBServiceException
	{
		List<EmployeePayrollData> employeePayrollListByName = new ArrayList<>();
		String query = "select * from Employee_Payroll where name = ?";
		try(Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name );
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
			{
				int id = resultSet.getInt(1);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender ,salary,start);
				employeePayrollListByName.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByName;
	}
	
	public void updateEmployeeSalary(String name , double salary) throws DBServiceException
	{
		String query = String.format("update Employee_Payroll set salary = %.2f where name = '%s';", salary , name);
		try(Connection con = new PayrollService().getConnection()) {
			Statement statement = con.createStatement();
			int result = statement.executeUpdate(query);
			empDataObj = getEmployeePayrollData( name);
			if(result > 0 && empDataObj != null)
				empDataObj.setSalary(salary);
		}catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}
	
	public void updateEmployeeSalaryUsingPreparedStatement(String name , double salary) throws DBServiceException
	{
		String query = "update Employee_Payroll set salary = ? where name = ?";
		try(Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int result = preparedStatement.executeUpdate();
			empDataObj = getEmployeePayrollData( name);
			if(result > 0 && empDataObj != null)
				empDataObj.setSalary(salary);
		}catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}	
	public EmployeePayrollData getEmployeePayrollData(String name) throws DBServiceException {
		return viewEmployeePayroll().stream()
								  .filter(e -> e.getName()
								  .equals(name))
								  .findFirst()
								  .orElse(null);
	}
	
	public boolean isEmpPayrollSyncedWithDB(String name) throws DBServiceException {
		try {
			return viewEmployeePayrollByName(name).get(0).equals(getEmployeePayrollData(name));
		} 
		catch (IndexOutOfBoundsException e) {
		} 
		catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return false;
	}
	
	public List<EmployeePayrollData> viewEmployeePayrollByJoinDateRange(LocalDate startDate , LocalDate endDate) throws DBServiceException
	{
		List<EmployeePayrollData> employeePayrollListByStartDate = new ArrayList<>();
		String query = "select * from Employee_Payroll where start_date between ? and  ?";
		try(Connection con = new PayrollService().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String gender = resultSet.getString(3);
				double salary = resultSet.getDouble(4);
				LocalDate start = resultSet.getDate(5).toLocalDate();
				empDataObj = new EmployeePayrollData(id, name, gender ,salary,start);
				employeePayrollListByStartDate.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBServiceException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByStartDate;
	}
}