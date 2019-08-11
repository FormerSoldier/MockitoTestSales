package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {
		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();
		
		if (salesId == null) {
			return;
		}

		SalesDao salesDao = new SalesDao();
		Sales sales = getSales(salesId, salesDao);
		if (sales == null) return;

		List<SalesReportData> reportDataList = getSalesReportDataBygetReportData(isSupervisor, filteredReportDataList, sales);
		List<SalesReportData> tempList = getSalesReportData(maxRow, reportDataList);
		filteredReportDataList = tempList;

		List<String> headers = getStringsHeaders(isNatTrade);

		SalesActivityReport report = this.generateReport(headers, reportDataList);
		
		EcmService ecmService = new EcmService();
		ecmService.uploadDocument(report.toXml());
		
	}

	public Sales getSales(String salesId, SalesDao salesDao) {
		Sales sales = salesDao.getSalesBySalesId(salesId);
		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return null;
		}
		return sales;
	}

	public List<SalesReportData> getSalesReportData(int maxRow, List<SalesReportData> reportDataList) {
		List<SalesReportData> tempList = new ArrayList<SalesReportData>();
		for (int i=0; i < reportDataList.size() || i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		return tempList;
	}

	public List<String> getStringsHeaders(boolean isNatTrade) {
		List<String> headers = null;
		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}
		return headers;
	}

	public List<SalesReportData> getSalesReportDataBygetReportData(boolean isSupervisor, List<SalesReportData> filteredReportDataList, Sales sales) {
		SalesReportDao salesReportDao = new SalesReportDao();
		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}
		return reportDataList;
	}

	public SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
