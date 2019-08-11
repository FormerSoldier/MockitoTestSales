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
	public void testGetSales_given_sales_EffectiveFrom_before_tody_and_effectoveTo_after_today_and_salesDao_thenSales(){
		Sales sale = mock(Sales.class);
		when(sale.getEffectiveFrom()).thenReturn(new Date(new Date().getTime() - 1000));
		when(sale.getEffectiveTo()).thenReturn(new Date(new Date().getTime() + 1000));

		SalesDao salesDao = mock(SalesDao.class);
		when(salesDao.getSalesBySalesId(any())).thenReturn(sale);

		SalesApp salesApp = new SalesApp();
		Sales result = salesApp.getSales(any(),salesDao);

		Assert.assertNotNull(result);
	}
}
