package world.willfrog.alphafrog.Service.Fund;

public interface FundPortfolioFetchService {

    int directFetchFundPortfolioByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                     int offset, int limit);
}
