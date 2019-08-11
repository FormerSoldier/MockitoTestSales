package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	/*@Test
	public void testGenerateReport() {
		
		SalesApp salesApp = new SalesApp();
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
	}*/

	@Mock
	SalesDao salesDao;
	@Mock
	SalesReportDao salesReportDao;
	@InjectMocks
	SalesApp salesApp ;

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

	@Test
	public void test_get_filtered_report_dataList_by_report_dataList_and_supervistor_given_filtered_report_dataList_and_true_then_return_the_size_of_list_is_2(){
		List<SalesReportData> filteredReportDataList = new ArrayList<>();
		filteredReportDataList.add(createSalesReportData("SalesActivity",true));
		filteredReportDataList.add(createSalesReportData("SalesActivity",false));
		filteredReportDataList.add(createSalesReportData("SalesStop",true));
		filteredReportDataList.add(createSalesReportData("SalesStop",false));

		SalesApp salesApp = new SalesApp();
		List<SalesReportData> result = salesApp.getFilteredReportDataListByReportDataListAndSupervistor(filteredReportDataList,true);

		Assert.assertEquals(2,result.size());
	}

	@Test
	public void test_get_filtered_report_dataList_by_report_dataList_and_supervistor_given_filtered_report_dataList_and_false_then_return_the_size_of_list_is_1(){
		List<SalesReportData> filteredReportDataList = new ArrayList<>();
		filteredReportDataList.add(createSalesReportData("SalesActivity",true));
		filteredReportDataList.add(createSalesReportData("SalesActivity",false));
		filteredReportDataList.add(createSalesReportData("SalesStop",true));
		filteredReportDataList.add(createSalesReportData("SalesStop",false));

		SalesApp salesApp = new SalesApp();
		List<SalesReportData> result = salesApp.getFilteredReportDataListByReportDataListAndSupervistor(filteredReportDataList,false);

		Assert.assertEquals(1,result.size());
	}

	@Test
	public void test_generate_sales_activity_report_given_null_1_true_true_then_return(){
		SalesApp salesApp = spy(new SalesApp());

		salesApp.generateSalesActivityReport(null,1, true,true);

		verify(salesApp,times(0)).getSales(any(),any());
		verify(salesApp,times(0)).getFilteredReportDataListByReportDataListAndSupervistor(any(),anyBoolean());
		verify(salesApp,times(0)).getSalesReportData(anyInt(),any());
		verify(salesApp,times(0)).getStringsHeaders(anyBoolean());
		verify(salesApp,times(0)).generateReport(any(),any());
	}

	@Test
	public void test_generate_sales_activity_report_given_empty_string_1_true_true(){
		SalesApp salesApp = spy(new SalesApp());

		doReturn(null).when(salesApp).getSales(anyString(),any());

		salesApp.generateSalesActivityReport("",1, true,true);

		verify(salesApp,times(1)).getSales(anyString(),any());
		verify(salesApp,times(0)).getFilteredReportDataListByReportDataListAndSupervistor(any(),anyBoolean());
		verify(salesApp,times(0)).getSalesReportData(anyInt(),any());
		verify(salesApp,times(0)).getStringsHeaders(anyBoolean());
		verify(salesApp,times(0)).generateReport(any(),any());
	}

	@Test
	public void test_generate_sales_activity_report_given_empty_string_0_true_true_then_all_function_execute(){
		SalesApp new_salesApp = spy(salesApp);
		Sales sales = createSaleWith(new Date(new Date().getTime() - 1000*60), new Date(new Date().getTime() + 1000*60));

		when(salesReportDao.getReportData(any())).thenReturn(new ArrayList<>());
		when(salesDao.getSalesBySalesId(anyString())).thenReturn(sales);
		doReturn(new SalesActivityReport()).when(new_salesApp).generateReport(anyList(),anyList());

        new_salesApp.generateSalesActivityReport("Dummy",0, true,true);

		verify(new_salesApp,times(1)).getSales(eq("Dummy"),any());
		verify(new_salesApp,times(1)).getFilteredReportDataListByReportDataListAndSupervistor(any(),anyBoolean());
		verify(new_salesApp,times(1)).getSalesReportData(anyInt(),any());
		verify(new_salesApp,times(1)).getStringsHeaders(anyBoolean());
		verify(new_salesApp,times(1)).generateReport(any(),any());
	}


	public SalesReportData createSalesReportData(String type, Boolean isConfidential){
		SalesReportData salesReportData = mock(SalesReportData.class);
		when(salesReportData.getType()).thenReturn(type);
		when(salesReportData.isConfidential()).thenReturn(isConfidential);
		return salesReportData;
	}

	public Sales createSaleWith(Date effectiveForm, Date effectiveTo){
		Sales sales = mock(Sales.class);
		when(sales.getEffectiveFrom()).thenReturn(effectiveForm);
		when(sales.getEffectiveTo()).thenReturn(effectiveTo);
		return sales;
	}
}
