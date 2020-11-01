package com.capg.payrollservice;


import static org.junit.Assert.assertEquals;

import java.util.List;

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

}