package com.capg.payrollservice;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PayrollServiceTest {
	static PayrollServiceDB payrollService;
	@BeforeClass
	public static void setUp()  {
		payrollService = new PayrollServiceDB();
	}

	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBServiceException{
		List<EmployeePayrollData> empPayrollList = payrollService.viewEmployeePayroll();
		assertEquals(3, empPayrollList.size());
	}
	
	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException{
		payrollService.updateEmployeeSalary("Terisa", 3000000.0);
		boolean isSynced = payrollService.isEmpPayrollSyncedWithDB("Terisa");
		assertTrue(isSynced);
	}

}