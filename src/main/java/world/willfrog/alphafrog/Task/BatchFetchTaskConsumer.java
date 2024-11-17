package world.willfrog.alphafrog.Task;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Service.Fund.FundBatchFetchService;
import world.willfrog.alphafrog.Service.Index.IndexBatchFetchService;

@Service
@Slf4j
public class BatchFetchTaskConsumer {

    private final IndexBatchFetchService indexBatchFetchService;
    private final FundBatchFetchService fundBatchFetchService;

    public BatchFetchTaskConsumer(IndexBatchFetchService indexBatchFetchService,
                                  FundBatchFetchService fundBatchFetchService) {
        this.indexBatchFetchService = indexBatchFetchService;
        this.fundBatchFetchService = fundBatchFetchService;
    }


    @KafkaListener( topics = "batch_fetch_topic", groupId = "alphafrog-v3-batch-fetch")
    public void listenBatchFetchTask(String message) {
        JSONObject rawMessageJSON;
        try {
            rawMessageJSON = JSONObject.parseObject(message);
        } catch (Exception e ){
            log.error("Error parsing batch fetch task's json config string");
            return;
        }


        String taskName;
        JSONObject taskParams;

        try {
            taskName = rawMessageJSON.getString("task_name");
            taskParams = rawMessageJSON.getJSONObject("task_params");
        } catch (Exception e) {
            log.error("Error parsing task name and params for batch fetch task");
            return;
        }

        int result;
        int taskSubtype;

        try{
            switch(taskName){
                case "index_quote":
                    taskSubtype = rawMessageJSON.getInteger("task_subtype");
                    if(taskSubtype == 1) {
                        result = indexBatchFetchService.batchFetchIndexDailyByDateRange(
                                taskParams.getLong("start_date_timestamp"),
                                taskParams.getLong("end_date_timestamp"));
                    } else {
                        throw new NumberFormatException("Task subtype should only be 1, 2, 3");
                    }
                    break;
                case "fund_nav":
                    result = fundBatchFetchService.batchFetchFundNavByTradeDate(
                            taskParams.getLong("trade_date_timestamp"));
                    break;
                default:
                    result = -3;
            }
        } catch (Exception e) {
            log.error("Error trying to execute batch fetch task", e);
            result = -2;
        }

        log.info("Task result: {}", result);
    }

}
