package world.willfrog.alphafrog.Task;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import world.willfrog.alphafrog.Common.TushareRequestUtils;
import world.willfrog.alphafrog.Service.IndexFetchService;

import com.alibaba.fastjson.JSONObject;



@Service
public class TaskConsumer {

  final
  IndexFetchService indexFetchService;


  public TaskConsumer(IndexFetchService indexFetchService) {
    this.indexFetchService = indexFetchService;
  }

  @KafkaListener(topics = "fetch_topic", groupId = "alphafrog-v3")
  public void listenFetchTasks(String message) throws JsonProcessingException {
    System.out.println("Received fetch task: " + message);
    // 转成json
    JSONObject rawMessageJson = JSONObject.parseObject(message);
    String taskName = rawMessageJson.getString("task_name");
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
      default:
        result = -1;
        break;
    }

    System.out.println("Task result: " + result);
  }

    
}
