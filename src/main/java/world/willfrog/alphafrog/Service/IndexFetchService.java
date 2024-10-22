package world.willfrog.alphafrog.Service;

import world.willfrog.alphafrog.Entity.IndexDaily;

import java.util.List;

public interface IndexFetchService {

  int directFetchIndexInfoByMarket(String market, int offset);

  int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate);


}
