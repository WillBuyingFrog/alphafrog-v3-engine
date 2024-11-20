package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Dao.Index.IndexInfoDao;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Service.Index.IndexBatchFetchService;
import world.willfrog.alphafrog.Service.Index.IndexFetchService;
import world.willfrog.alphafrog.Service.Index.IndexQuoteFetchService;

import java.util.List;

@Service
@Slf4j
public class IndexBatchFetchServiceImpl implements IndexBatchFetchService {


    private final DateConvertUtils dateConvertUtils;

    private final IndexQuoteFetchService indexQuoteFetchService;
    private final IndexFetchService indexFetchService;

    public IndexBatchFetchServiceImpl(DateConvertUtils dateConvertUtils,
                                      IndexQuoteFetchService indexQuoteFetchService,
                                      IndexFetchService indexFetchService) {
        this.dateConvertUtils = dateConvertUtils;
        this.indexQuoteFetchService = indexQuoteFetchService;
        this.indexFetchService = indexFetchService;
    }

    @Override
    public int batchFetchIndexDailyByDateRange(long startDateTimestamp, long endDateTimestamp, int queryInterval,
                                               int offset, int limit) {

        long DAY_TO_MILLISECOND = 24L * 60 * 60 * 1000;
        int res = 0;

        try{
            String startDate = dateConvertUtils.convertTimestampToString(startDateTimestamp, "yyyyMMdd");
            String endDate = dateConvertUtils.convertTimestampToString(endDateTimestamp, "yyyyMMdd");

            List<String> allIndexTsCodes = indexFetchService.getAllIndexTsCodes();

            log.info("Fetching all index daily quote from date {} to {}", startDate, endDate);

            for(int i = offset; i < Math.min(limit, allIndexTsCodes.size()); i++){
                String tsCode = allIndexTsCodes.get(i);
                log.info("Fetching index daily quote for tsCode: {}", tsCode);

                res += indexQuoteFetchService.directFetchIndexDailyByTsCodeAndDateRange(tsCode, startDate, endDate, 0, 10000);
                if(res < 0){
                    log.error("Failed to fetch index daily quote for tsCode: {}", tsCode);
                    return -1;
                }
                try {
                    Thread.sleep(queryInterval);
                } catch (InterruptedException e) {
                    log.error("Sleep interrupted while batch fetching index daily data", e);
                    Thread.currentThread().interrupt();
                }
            }

        } catch (Exception e) {
            log.error("Error batch fetching index daily data");
            return -1;
        }

        return res;
    }
}
