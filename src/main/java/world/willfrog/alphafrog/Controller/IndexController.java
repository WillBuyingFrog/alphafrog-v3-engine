package world.willfrog.alphafrog.Controller;


import com.alibaba.fastjson2.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;
import world.willfrog.alphafrog.Service.Index.IndexInformationUserService;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Entity.Index.IndexDaily;

@Controller
@RequestMapping("/index")
public class IndexController {

    private final IndexInformationUserService indexInformationUserService;

    private final DateConvertUtils dateConvertUtils;

    public IndexController(IndexInformationUserService indexInformationUserService, DateConvertUtils dateConvertUtils) {
        this.indexInformationUserService = indexInformationUserService;
        this.dateConvertUtils = dateConvertUtils;
    }

    @GetMapping("/get/info")
    public ResponseEntity<String> getIndexInfo(@RequestParam(name = "ts_code", required = false) String tsCode,
                                               @RequestParam(name = "query_name", required = false) String queryName) {

        if(tsCode == null && queryName == null) {
            return ResponseEntity.badRequest().body("ts_code or query_name is required");
        }
        JSONObject ret = new JSONObject();
        // 优先按照指数代码进行搜索
        if(tsCode != null) {
            List<IndexInfo> indexInfoList = indexInformationUserService.getIndexInfoByTsCode(tsCode);
        
            // 默认只返回第一个结果        
            ret.put("result", indexInfoList.get(0));
        } else {
            // 按照指数名称进行搜索
            List<IndexInfo> indexInfoList = indexInformationUserService.getIndexInfoByNames(queryName);

            ret.put("result", indexInfoList);
        }
        return ResponseEntity.ok(ret.toJSONString());
    }

    @GetMapping("/get/search")
    public ResponseEntity<String> searchIndexInfo(@RequestParam(name = "query") String query,
                                                  @RequestParam(name = "page") int page,
                                                  @RequestParam(name = "page_size") int pageSize) {
        if(query == null) {
            return ResponseEntity.badRequest().body("query is required");
        }

        List<IndexInfo> indexInfoList = indexInformationUserService.searchIndexInfo(query, page, pageSize);

        JSONObject ret = new JSONObject();
        ret.put("result", indexInfoList);

        return ResponseEntity.ok(ret.toJSONString());
    }


    @GetMapping("/get/daily")
    public ResponseEntity<String> getIndexDaily(@RequestParam(name = "ts_code") String tsCode,
                                                @RequestParam(name = "start_date", required = false) String startDate,
                                                @RequestParam(name = "end_date", required = false) String endDate) {
        
        long startDateTimestamp;
        long endDateTimestamp;
        
        if(startDate != null && endDate != null) {
            startDateTimestamp = dateConvertUtils.convertDateStrToLong(startDate, "yyyyMMdd");
            endDateTimestamp = dateConvertUtils.convertDateStrToLong(endDate, "yyyyMMdd");
        } else {
            // 默认查询近一个月的日线数据
            startDateTimestamp = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000;
            endDateTimestamp = System.currentTimeMillis();
        }
        
        List<IndexDaily> indexDailyList = indexInformationUserService.getIndexDailyByTsCodeAndDateRange(tsCode,
                startDateTimestamp, endDateTimestamp);


        JSONObject ret = new JSONObject();
        ret.put("result", indexDailyList);

        return ResponseEntity.ok(ret.toJSONString());
    }

}