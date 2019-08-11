package sales;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SalesAppTest {

	@Test
	public void testGenerateReport() {
		
		SalesApp salesApp = new SalesApp();
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
	}

	@Test
	public void testGetSales_given_sales_effectiveFrom_before_tody_and_effectiveTo_after_today_and_salesDao_then_return_sales(){
		Sales sales = createSaleWith(new Date(new Date().getTime() - 1000), new Date(new Date().getTime() + 1000));

		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(any())).thenReturn(sales);

		SalesApp salesApp = new SalesApp();
		Sales result = salesApp.getSales(any(),salesDao);

		Assert.assertNotNull(result);
	}

	@Test
	public void testGetSales_given_sales_effectiveForm_before_today_sales_effectiveTo_before_today_and_salesDao_then_return_null(){
		Sales sales = createSaleWith(new Date(new Date().getTime() - 1000), new Date(new Date().getTime() - 1000));

		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(any())).thenReturn(sales);

		SalesApp salesApp = new SalesApp();
		Sales result = salesApp.getSales(any(),salesDao);

		Assert.assertNull(result);
	}

	@Test
	public void testGetSales_given_sales_effectiveForm_after_today_sales_effectiveTo_after_today_and_salesDao_then_return_null(){
		Sales sales = createSaleWith(new Date(new Date().getTime() + 1000), new Date(new Date().getTime() + 1000));

		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(any())).thenReturn(sales);

		SalesApp salesApp = new SalesApp();
		Sales result = salesApp.getSales(any(),salesDao);

		Assert.assertNull(result);
	}

	@Test
	public void test_get_sales_report_data_given_max_row_less_than_or_equals_the_size_of_report_data_list_and_report_data_list_then_return_the_equlas_list_with_report_data_list(){
		SalesApp salesApp = new SalesApp();
		List<SalesReportData> reportDataList = new ArrayList<>();

		for(int i = 0 ; i < 10; i++)
			reportDataList.add(mock(SalesReportData.class));

		List<SalesReportData> tempList = salesApp.getSalesReportData(reportDataList.size()-1,reportDataList);

		Assert.assertEquals(10,tempList.size());
	}

	@Test
	public void test_get_strings_headers_given_true_then_return_the_list_include_elements_SalesID_SalesName_Activity_Time(){
		SalesApp salesApp = new SalesApp();
		List<String> headers = salesApp.getStringsHeaders(true);

		List<String> expected = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		Assert.assertEquals(expected,headers);
	}

	@Test
	public void test_get_strings_headers_given_false_then_return_the_list_include_elements_SalesID_SalesName_Activity_LocalTime(){
		SalesApp salesApp = new SalesApp();
		List<String> headers = salesApp.getStringsHeaders(false);

		List<String> expected = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		Assert.assertEquals(expected,headers);
	}



	public Sales createSaleWith(Date effectiveForm, Date effectiveTo){
		Sales sales = mock(Sales.class);
		when(sales.getEffectiveFrom()).thenReturn(effectiveForm);
		when(sales.getEffectiveTo()).thenReturn(effectiveTo);
		return sales;
	}
}
