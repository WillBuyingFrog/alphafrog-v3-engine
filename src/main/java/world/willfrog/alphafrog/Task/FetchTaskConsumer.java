package world.willfrog.alphafrog.Task;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import world.willfrog.alphafrog.Service.Fund.FundInfoFetchService;
import world.willfrog.alphafrog.Service.Fund.FundNavFetchService;
import world.willfrog.alphafrog.Service.Index.IndexFetchService;
import world.willfrog.alphafrog.Service.Fund.FundPortfolioFetchService;

import com.alibaba.fastjson.JSONObject;
import world.willfrog.alphafrog.Service.Index.IndexQuoteFetchService;
import world.willfrog.alphafrog.Service.Index.IndexWeightFetchService;


@Service
public class FetchTaskConsumer {

    final IndexFetchService indexFetchService;
    final IndexQuoteFetchService indexQuoteFetchService;
    final IndexWeightFetchService indexWeightFetchService;

    final FundNavFetchService fundNavFetchService;
    final FundPortfolioFetchService fundPortfolioFetchService;
    final FundInfoFetchService fundInfoFetchService;


    public FetchTaskConsumer(IndexFetchService indexFetchService,
                             IndexQuoteFetchService indexQuoteFetchService,
                             IndexWeightFetchService indexWeightFetchService,
                             FundNavFetchService fundNavFetchService,
                             FundPortfolioFetchService fundPortfolioFetchService,
                             FundInfoFetchService fundInfoFetchService) {
        this.indexFetchService = indexFetchService;
        this.indexQuoteFetchService = indexQuoteFetchService;
        this.indexWeightFetchService = indexWeightFetchService;


        this.fundNavFetchService = fundNavFetchService;
        this.fundPortfolioFetchService = fundPortfolioFetchService;
        this.fundInfoFetchService = fundInfoFetchService;
    }

    @KafkaListener(topics = "fetch_topic", groupId = "alphafrog-v3-fetch")
    public void listenFetchTasks(String message) throws JsonProcessingException {
        System.out.println("Received fetch task: " + message);
        // 转成json
        JSONObject rawMessageJson = JSONObject.parseObject(message);
        try{
            String taskName = rawMessageJson.getString("task_name");
            int taskSubtype = rawMessageJson.getInteger("task_subtype");
            JSONObject taskParams = rawMessageJson.getJSONObject("task_params");

            int result;

            switch (taskName) {
                case "index_basic":
                    result = indexFetchService.directFetchIndexInfoByMarket(taskParams.getString("market"),
                            taskParams.getInteger("offset"));
                    break;
                case "index_quote":
                    switch (taskSubtype) {
                        case 1:
                            result = indexQuoteFetchService.directFetchIndexDailyByTsCodeAndDateRange(
                                    taskParams.getString("ts_code"),
                                    taskParams.getString("start_date"), taskParams.getString("end_date"),
                                    taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                            break;
                        case 2:
                            result = indexQuoteFetchService.directFetchIndexDailyByTradeDate(
                                    taskParams.getString("trade_date"),
                                    taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                            break;
                        default:
                            // Handle unknown taskSubtype
                            result = -1;
                            break;
                    }
                    break;

                case "index_weight":
                    if (taskSubtype == 1) {
                        result = indexWeightFetchService.directFetchIndexWeightByIndexCodeAndDateRange(
                                taskParams.getString("index_code"),
                                taskParams.getString("start_date"), taskParams.getString("end_date"),
                                taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    } else if (taskSubtype == 2) {
                        result = indexWeightFetchService.directFetchIndexWeightByTsCodeAndDateRange(
                                taskParams.getString("ts_code"),
                                taskParams.getString("start_date"), taskParams.getString("end_date"),
                                taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    } else {
                        // 不存在这样的子任务类型
                        result = -1;
                    }
                    break;
                case "fund_nav":
                    if (taskSubtype == 1) {
                        result = fundNavFetchService.directFetchFundNavByTsCodeAndDateRange(taskParams.getString("ts_code"),
                                taskParams.getString("start_date"), taskParams.getString("end_date"),
                                taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    } else if (taskSubtype == 2) {
                        // 按照净值公布时间和上市市场类型爬取
                        result = fundNavFetchService.directFetchFundNavByNavDateAndMarket(taskParams.getString("nav_date"),
                                taskParams.getString("market"), taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    } else {
                        // 不存在这样的子任务类型
                        result = -1;
                    }
                    break;
                case "fund_portfolio":
                    // 处理基金持仓任务
                    result = fundPortfolioFetchService.directFetchFundPortfolioByTsCodeAndDateRange(taskParams.getString("ts_code"),
                            taskParams.getString("start_date"), taskParams.getString("end_date"),
                            taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    break;

                case "fund_info":
                    if (taskSubtype == 1) {
                        result = fundInfoFetchService.directFetchFundInfoByMarket(taskParams.getString("market"),
                                taskParams.getInteger("offset"), taskParams.getInteger("limit"));

                    } else if (taskSubtype == 2) {
                        result = fundInfoFetchService.directFetchFundInfoByTsCode(taskParams.getString("ts_code"),
                                taskParams.getInteger("offset"), taskParams.getInteger("limit"));
                    } else {
                        // 不存在这样的子任务类型
                        result = -1;
                    }
                    break;
                default:
                    result = -1;
                    break;
            }

            System.out.println("Task result: " + result);
        } catch (Exception e){
            System.out.println("Error occurred while processing fetch task");
            e.printStackTrace();
        }

    }


}
