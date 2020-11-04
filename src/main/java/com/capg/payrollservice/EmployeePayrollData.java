package com.capg.payrollservice;

import java.time.LocalDate;

public class EmployeePayrollData {
	private int emp_id;
	private String name;
	private double salary;
	private LocalDate start_date;
	private String gender;

	public EmployeePayrollData(int emp_id, String name, double salary, LocalDate start) {
		super();
		this.emp_id = emp_id;
		this.name = name;
		this.salary = salary;
		this.start_date = start;
	}
	
	public EmployeePayrollData(String name,String gender, double salary, LocalDate start) {
		super();
		this.name = name;
		this.salary = salary;
		this.start_date = start;
		this.gender = gender;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate start) {
		this(id, name, salary, start);
		this.setGender(gender);
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [emp_id=" + emp_id + ", name=" + name + ", salary=" + salary + ", start_date="
				+ start_date + "]";
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		if (emp_id != other.emp_id)
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (start_date == null) {
			if (other.start_date != null)
				return false;
		} else if (!start_date.equals(other.start_date))
			return false;
		return true;
	}
}