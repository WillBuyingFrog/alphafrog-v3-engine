package world.willfrog.alphafrog.Service.Index;


import world.willfrog.alphafrog.Entity.Index.IndexInfo;

import java.util.List;

public interface IndexFetchService {

    int directFetchIndexInfoByMarket(String market, int offset);

    int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate);

    List<String> getAllIndexTsCodes();

    int getIndexCount();

}
