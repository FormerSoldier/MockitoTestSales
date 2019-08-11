package sales;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

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



	public Sales createSaleWith(Date effectiveForm, Date effectiveTo){
		Sales sales = mock(Sales.class);
		when(sales.getEffectiveFrom()).thenReturn(effectiveForm);
		when(sales.getEffectiveTo()).thenReturn(effectiveTo);
		return sales;
	}
}
