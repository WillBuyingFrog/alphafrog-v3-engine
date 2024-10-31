package world.willfrog.alphafrog.Service.Fund;

public interface FundNavFetchService {

    int directFetchFundNavByTsCodeAndDateRange(String tsCode, String startDate, String endDate, int offset, int limit);

    int directFetchFundNavByNavDateAndMarket(String navDate, String Market, int offset, int limit);
}

