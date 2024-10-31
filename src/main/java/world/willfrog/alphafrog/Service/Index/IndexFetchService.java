package world.willfrog.alphafrog.Service.Index;

public interface IndexFetchService {

    int directFetchIndexInfoByMarket(String market, int offset);

    int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate);


}
