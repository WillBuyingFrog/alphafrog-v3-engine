package world.willfrog.alphafrog.Task;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import world.willfrog.alphafrog.Service.FundNavFetchService;
import world.willfrog.alphafrog.Service.IndexFetchService;
import world.willfrog.alphafrog.Service.FundPortfolioFetchService;

import com.alibaba.fastjson.JSONObject;



@Service
public class TaskConsumer {

  final IndexFetchService indexFetchService;

  final FundNavFetchService fundNavFetchService;

  final FundPortfolioFetchService fundPortfolioFetchService;


  public TaskConsumer(IndexFetchService indexFetchService, FundNavFetchService fundNavFetchService,
                      FundPortfolioFetchService fundPortfolioFetchService) {
    this.indexFetchService = indexFetchService;
    this.fundNavFetchService = fundNavFetchService;
    this.fundPortfolioFetchService = fundPortfolioFetchService;
  }

  @KafkaListener(topics = "fetch_topic", groupId = "alphafrog-v3")
  public void listenFetchTasks(String message) throws JsonProcessingException {
    System.out.println("Received fetch task: " + message);
    // 转成json
    JSONObject rawMessageJson = JSONObject.parseObject(message);
    String taskName = rawMessageJson.getString("task_name");
    int taskSubtype = rawMessageJson.getInteger("task_subtype");
    JSONObject taskParams = rawMessageJson.getJSONObject("task_params");

    int result;

    switch (taskName) {
      case "index_daily":
        result = indexFetchService.directFetchIndexDailyByTsCodeAndDateRange(taskParams.getString("ts_code"),
                taskParams.getString("start_date"), taskParams.getString("end_date"));
        break;
      case "index_basic":
        result = indexFetchService.directFetchIndexInfoByMarket(taskParams.getString("market"),
                taskParams.getInteger("offset"));
        break;
      case "fund_nav":
        if(taskSubtype == 1) {
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
      default:
        result = -1;
        break;
    }

    System.out.println("Task result: " + result);
  }

    
}
