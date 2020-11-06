package com.capg.payrollservice;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PayrollServiceTest {
	static PayrollServiceDB payrollService;
	static Map<String, Double> empDataByGender;
	static List<EmployeePayrollData> empPayrollList;

	@BeforeClass
	public static void setUp() {
		payrollService = new PayrollServiceDB();
		empDataByGender = new HashMap<>();
		empPayrollList = new ArrayList<>();
	}

	@Ignore
	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = payrollService.viewEmployeePayroll();
		Assert.assertEquals(3, empPayrollList.size());
	}

	@Ignore
	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		payrollService.updateEmployeeSalary("Terisa", 3000000.0);
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Terisa");
		assertTrue(isSynced);
	}

	@Ignore
	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDBUsingPreparedStatement()
			throws DBServiceException {
		payrollService.updateEmployeeSalaryUsingPreparedStatement("Terisa", 3000000.0);
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Terisa");
		assertTrue(isSynced);
	}

	@Ignore
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = payrollService
				.viewEmployeePayrollByJoinDateRange(LocalDate.of(2018, 02, 01), LocalDate.now());
		Assert.assertEquals(2, empPayrollList.size());
	}

	@Ignore
	@Test
	public void givenEmployeeDB_WhenRetrievedSum_ShouldReturnSumGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "sum");
		Assert.assertEquals(400000, empDataByGender.get("M"), 0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"), 0.0);
	}

	@Ignore
	@Test
	public void givenEmployeeDB_WhenRetrievedAvg_ShouldReturnAvgByGroupedGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "avg");
		Assert.assertEquals(200000, empDataByGender.get("M"), 0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"), 0.0);
	}

	@Ignore
	@Test
	public void givenEmployeeDB_WhenRetrievedMax_ShouldReturnMaxGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "max");
		Assert.assertEquals(300000, empDataByGender.get("M"), 0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"), 0.0);
	}

	@Ignore
	@Test
	public void givenEmployeeDB_WhenRetrievedMin_ShouldReturnMinGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "min");
		Assert.assertEquals(100000, empDataByGender.get("M"), 0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"), 0.0);
	}

	@Ignore
	@Test
	public void givenEmployeeDB_WhenRetrievedCount_ShouldReturnCountGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "count");
		Assert.assertEquals(2, empDataByGender.get("M"), 0.0);
		Assert.assertEquals(1, empDataByGender.get("F"), 0.0);
	}

	@Ignore
	@Test
	public void insertedNewEmployee_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		payrollService.insertNewEmployeeToDB("Mark", "M", .0, LocalDate.now(), 101, "Sales");
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Mark");
		assertTrue(isSynced);
	}

	@Ignore
	@Test
	public void givenEmployeeId_WhenDeletedUsing_ShouldSyncWithDB() throws DBServiceException {
		payrollService.removeEmployeeFromDB(2);
		Assert.assertEquals(2, empPayrollList.size());

	}

	@Ignore
	@Test
	public void givenEmployeeData_ShouldPrintInstanceTime_ToConsole() throws DBServiceException {
		EmployeePayrollData[] arrayOfEmp = { new EmployeePayrollData("Jeff Bezos", "M", 100000.0, LocalDate.now()),
				new EmployeePayrollData("Bill Gates", "M", 200000.0, LocalDate.now()),
				new EmployeePayrollData("Mark Zuckerberg", "M", 300000.0, LocalDate.now()),
				new EmployeePayrollData("Sundar", "M", 600000.0, LocalDate.now()),
				new EmployeePayrollData("Mukesh", "M", 500000.0, LocalDate.now()),
				new EmployeePayrollData("Anil", "M", 300000.0, LocalDate.now()) };
		Instant start = Instant.now();
		payrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: " + java.time.Duration.between(start, end));
	}

	@Ignore
	@Test
	public void givenEmployeeData_ShouldPrintInstanceTime_ToConsoleUsingThreads() throws DBServiceException {
		EmployeePayrollData[] arrayOfEmp = { new EmployeePayrollData("Jeff Bezos", "M", 100000.0, LocalDate.now()),
				new EmployeePayrollData("Bill Gates", "M", 200000.0, LocalDate.now()),
				new EmployeePayrollData("Mark Zuckerberg", "M", 300000.0, LocalDate.now()),
				new EmployeePayrollData("Sundar", "M", 600000.0, LocalDate.now()),
				new EmployeePayrollData("Mukesh", "M", 500000.0, LocalDate.now()),
				new EmployeePayrollData("Anil", "M", 300000.0, LocalDate.now()) };
		Instant start = Instant.now();
		payrollService.addEmployeeToPayrollUsingThreads(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: " + java.time.Duration.between(start, end));
	}

	@Test
	public void givenEmployeePayrollData_ShouldPrintInstanceTime_ToConsoleUsingThreads() throws DBServiceException {
		EmployeePayrollData[] arrayOfEmp = {
				new EmployeePayrollData("Jeff Bezos", "M", 100000.0, LocalDate.now(), 501, "Finance"),
				new EmployeePayrollData("Bill Gates", "M", 200000.0, LocalDate.now(), 502, "Marketing"),
				new EmployeePayrollData("Mark Zuckerberg", "M", 300000.0, LocalDate.now(), 503, "Consultancy"),
				new EmployeePayrollData("Sundar", "M", 600000.0, LocalDate.now(), 504, "Shares"),
				new EmployeePayrollData("Mukesh", "M", 500000.0, LocalDate.now(), 505, "Management"),
				new EmployeePayrollData("Anil", "M", 300000.0, LocalDate.now(), 506, "Promotion") };
		Instant start = Instant.now();
		payrollService.insertEmployeeToPayrollDetailsUsingThreads(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread: " + java.time.Duration.between(start, end));
	}

	@Test
	public void givenEmployeeData_When_UpdatedShouldSyncWithDB() {
		EmployeePayrollData[] arrayOfEmp = { new EmployeePayrollData("Jeff Bezos", 200000.0),
				new EmployeePayrollData("Bill Gates", 300000.0), new EmployeePayrollData("Mark Zuckerberg", 400000.0),
				new EmployeePayrollData("Sundar", 700000.0), new EmployeePayrollData("Mukesh", 800000.0),
				new EmployeePayrollData("Anil", 900000.0) };
		Instant start = Instant.now();
		payrollService.updateEmployeeDataUsingThreads(Arrays.asList(arrayOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration With Thread: " + java.time.Duration.between(start, end));
	}
}