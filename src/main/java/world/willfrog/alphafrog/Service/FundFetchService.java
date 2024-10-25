package world.willfrog.alphafrog.Service;

public interface FundFetchService {

  int directFetchFundNavByTsCodeAndDateRange(String tsCode, String startDate, String endDate, int offset, int limit);

  int directFetchFundNavByNavDateAndMarket(String navDate, String Market, int offset, int limit);
}

