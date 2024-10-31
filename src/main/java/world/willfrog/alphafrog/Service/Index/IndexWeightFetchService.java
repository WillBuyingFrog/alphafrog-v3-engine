package world.willfrog.alphafrog.Service.Index;

public interface IndexWeightFetchService {

    int directFetchIndexWeightByIndexCodeAndDateRange(String indexCode, String startDate, String endDate,
                                                      int offset, int limit);

    int directFetchIndexWeightByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                   int offset, int limit);
}
