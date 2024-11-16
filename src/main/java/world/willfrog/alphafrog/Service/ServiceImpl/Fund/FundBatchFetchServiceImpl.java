package world.willfrog.alphafrog.Service.ServiceImpl.Fund;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Service.Fund.FundBatchFetchService;
import world.willfrog.alphafrog.Service.Fund.FundNavFetchService;

@Service
@Slf4j
public class FundBatchFetchServiceImpl implements FundBatchFetchService {


    private final DateConvertUtils dateConvertUtils;

    private final FundNavFetchService fundNavFetchService;


    public FundBatchFetchServiceImpl(DateConvertUtils dateConvertUtils, FundNavFetchService fundNavFetchService) {
        this.dateConvertUtils = dateConvertUtils;
        this.fundNavFetchService = fundNavFetchService;
    }


    @Override
    public int batchFetchFundNavByTradeDate(long tradeDateTimestamp) {

        String tradeDateStr;
        try{
            tradeDateStr = dateConvertUtils.convertTimestampToString(tradeDateTimestamp, "yyyyMMdd");
        } catch (Exception e) {
            log.error("Failed to convert timestamp to string", e);
            return -1;
        }

        int result;
        log.info("Start to fetch fund nav data for trade date: {}", tradeDateStr);

        result = fundNavFetchService.directFetchFundNavByNavDateAndMarket(tradeDateStr, null, 0, 15000);
        if(result < 0){
            log.error("Error fetching fund nav data");
            return result;
        }

        return result;
    }

}
