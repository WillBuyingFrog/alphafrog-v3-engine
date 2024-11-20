package world.willfrog.alphafrog.Service.Index;

public interface IndexBatchFetchService {

    public int batchFetchIndexDailyByDateRange(long startDate, long endDate, int requestInterval, int offset, int limit);
}
