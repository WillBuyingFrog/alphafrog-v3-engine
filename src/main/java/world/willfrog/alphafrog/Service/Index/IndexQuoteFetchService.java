package world.willfrog.alphafrog.Service.Index;

public interface IndexQuoteFetchService {

    int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                  int offset, int limit);

    int directFetchIndexDailyByTradeDate(String tradeDate, int offset, int limit);

    int directFetchIndexWeeklyByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                  int offset, int limit);

//    int directFetchIndexMonthlyByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
//                                                    int offset, int limit);
}
