package world.willfrog.alphafrog.Service;

public interface FundPortfolioFetchService {

  int directFetchFundPortfolioByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                   int offset, int limit);
}
