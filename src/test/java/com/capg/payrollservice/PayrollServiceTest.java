package com.capg.payrollservice;

import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayrollServiceTest {
	static PayrollServiceDB payrollService;
	static Map<String, Double> empDataByGender;
	static List<EmployeePayrollData> empPayrollList;
	
	@BeforeClass
	public static void setUp()  {
		payrollService = new PayrollServiceDB();
		empDataByGender = new HashMap<>();
		empPayrollList = new ArrayList<>();
	}

	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = payrollService.viewEmployeePayroll();
		Assert.assertEquals(3, empPayrollList.size());
	}

	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException {
		payrollService.updateEmployeeSalary("Terisa", 3000000.0);
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Terisa");
		assertTrue(isSynced);
	}

	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDBUsingPreparedStatement()
			throws DBServiceException {
		payrollService.updateEmployeeSalaryUsingPreparedStatement("Terisa", 3000000.0);
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Terisa");
		assertTrue(isSynced);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException {
		List<EmployeePayrollData> empPayrollList = payrollService
				.viewEmployeePayrollByJoinDateRange(LocalDate.of(2018,02,01), LocalDate.now());
		Assert.assertEquals(2, empPayrollList.size());
	}
	
	@Test
	public void givenEmployeeDB_WhenRetrievedSum_ShouldReturnSumGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary" , "sum");
		Assert.assertEquals(400000, empDataByGender.get("M"),0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"),0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedAvg_ShouldReturnAvgByGroupedGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary" , "avg");
		Assert.assertEquals(200000, empDataByGender.get("M"),0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"),0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedMax_ShouldReturnMaxGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary" , "max");
		Assert.assertEquals(300000, empDataByGender.get("M"),0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"),0.0);
	}
	
	@Test
	public void givenEmployeeDB_WhenRetrievedMin_ShouldReturnMinGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary" , "min");
		Assert.assertEquals(100000, empDataByGender.get("M"),0.0);
		Assert.assertEquals(3000000, empDataByGender.get("F"),0.0);
	}

	@Test
	public void givenEmployeeDB_WhenRetrievedCount_ShouldReturnCountGroupedByGender() throws DBServiceException {
		empDataByGender = payrollService.viewEmployeeDataGroupedByGender("salary", "count");
		Assert.assertEquals(2, empDataByGender.get("M"),0.0);
		Assert.assertEquals(1, empDataByGender.get("F"),0.0);
	}

}