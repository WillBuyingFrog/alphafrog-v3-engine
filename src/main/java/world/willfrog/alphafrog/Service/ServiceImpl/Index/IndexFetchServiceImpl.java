package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Dao.Index.IndexInfoDao;
import world.willfrog.alphafrog.Entity.Index.IndexDaily;
import world.willfrog.alphafrog.Dao.Index.IndexDailyDao;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Service.Index.IndexFetchService;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class IndexFetchServiceImpl implements IndexFetchService {

    @Autowired
    private TuShareRequestUtils tuShareRequestUtils;

    @Autowired
    private DateConvertUtils dateConvertUtils;

    @Autowired
    private IndexDailyDao indexDailyDao;

    @Autowired
    private IndexInfoDao indexInfoDao;

    @Override
    public int directFetchIndexInfoByMarket(String market, int offset) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_basic");
        queryParams.put("market", market);
        queryParams.put("offset", offset);
        params.put("params", queryParams);
        params.put("fields", "ts_code,name,fullname,market,publisher,index_type," +
                "category,base_date,base_point,list_date,weight_rule,desc,exp_date");

        JSONObject res = tuShareRequestUtils.createTusharePostRequest(params);

        if (res == null) {
            return -2;
        }

        JSONArray fields = res.getJSONObject("data").getJSONArray("fields");
        JSONArray data = res.getJSONObject("data").getJSONArray("items");

        List<IndexInfo> indexInfoList = new ArrayList<>();


        try {
            for (int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                IndexInfo indexInfo = new IndexInfo();

                for (int j = 0; j < fields.size(); j++) {
                    String field = fields.getString(j);
                    switch (field) {
                        case "ts_code":
                            indexInfo.setTsCode(item.getString(j));
                            break;
                        case "name":
                            indexInfo.setName(item.getString(j));
                            break;
                        case "fullname":
                            indexInfo.setFullName(item.getString(j));
                            break;
                        case "market":
                            indexInfo.setMarket(item.getString(j));
                            break;
                        case "publisher":
                            indexInfo.setPublisher(item.getString(j));
                            break;
                        case "index_type":
                            indexInfo.setIndexType(item.getString(j));
                            break;
                        case "category":
                            indexInfo.setCategory(item.getString(j));
                            break;
                        case "base_date":
                            String baseDateStr = item.getString(j);
                            if (baseDateStr == null) {
                                indexInfo.setBaseDate(null);
                            } else {
                                Long baseDate = dateConvertUtils.convertDateStrToLong(baseDateStr, "yyyyMMdd");
                                indexInfo.setBaseDate(baseDate);
                            }
                            break;
                        case "base_point":
                            BigDecimal basePointDecimal = item.getBigDecimal(j);
                            if (basePointDecimal == null) {
                                indexInfo.setBasePoint(null);
                            } else {
                                indexInfo.setBasePoint(basePointDecimal.doubleValue());
                            }
                            break;
                        case "list_date":
                            String listDateStr = item.getString(j);
                            if (listDateStr == null) {
                                indexInfo.setListDate(null);
                            } else {
                                Long listDate = dateConvertUtils.convertDateStrToLong(listDateStr, "yyyyMMdd");
                                indexInfo.setListDate(listDate);
                            }
                            break;
                        case "weight_rule":
                            indexInfo.setWeightRule(item.getString(j));
                            break;
                        case "desc":
                            indexInfo.setDesc(item.getString(j));
                            break;
                        case "exp_date":
                            String expDateStr = item.getString(j);
                            if (expDateStr == null) {
                                indexInfo.setExpDate(null);
                            } else {
                                Long expDate = dateConvertUtils.convertDateStrToLong(expDateStr, "yyyyMMdd");
                                indexInfo.setExpDate(expDate);
                            }

                            break;
                        default:
                            break;
                    }
                }
                indexInfoList.add(indexInfo);
            }
        } catch (Exception e) {
            System.out.println("Error occured while fetching index info");
            e.printStackTrace();
        }


        for (IndexInfo indexInfo : indexInfoList) {
            indexInfoDao.insertIndexInfo(indexInfo);
        }

        return indexInfoList.size();
    }

    @Override
    public int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_daily");
        queryParams.put("ts_code", tsCode);
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        params.put("params", queryParams);
        params.put("fields", "ts_code,trade_date,close,open,high,low,pre_close,change,pct_chg,vol,amount");

        JSONObject res = tuShareRequestUtils.createTusharePostRequest(params);

        if (res == null) {
            return -2;
        }

        JSONArray fields = res.getJSONObject("data").getJSONArray("fields");
        JSONArray data = res.getJSONObject("data").getJSONArray("items");

        List<IndexDaily> indexDailyList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            JSONArray item = data.getJSONArray(i);
            IndexDaily indexDaily = new IndexDaily();

            for (int j = 0; j < fields.size(); j++) {
                String field = fields.getString(j);
                switch (field) {
                    case "ts_code":
                        indexDaily.setTsCode(item.getString(j));
                        break;
                    case "trade_date":
                        String tradeDateStr = item.getString(j);
                        Long tradeDate = dateConvertUtils.convertDateStrToLong(tradeDateStr, "yyyyMMdd");
                        indexDaily.setTradeDate(tradeDate);
                        break;
                    case "close":
                        indexDaily.setClose(item.getBigDecimal(j).doubleValue());
                        break;
                    case "open":
                        indexDaily.setOpen(item.getBigDecimal(j).doubleValue());
                        break;
                    case "high":
                        indexDaily.setHigh(item.getBigDecimal(j).doubleValue());
                        break;
                    case "low":
                        indexDaily.setLow(item.getBigDecimal(j).doubleValue());
                        break;
                    case "pre_close":
                        indexDaily.setPreClose(item.getBigDecimal(j).doubleValue());
                        break;
                    case "change":
                        indexDaily.setChange(item.getBigDecimal(j).doubleValue());
                        break;
                    case "pct_chg":
                        indexDaily.setPctChg(item.getBigDecimal(j).doubleValue());
                        break;
                    case "vol":
                        indexDaily.setVol(item.getBigDecimal(j).doubleValue());
                        break;
                    case "amount":
                        indexDaily.setAmount(item.getBigDecimal(j).doubleValue());
                        break;
                    default:
                        break;
                }
            }
            indexDailyList.add(indexDaily);
        }

        for (IndexDaily indexDaily : indexDailyList) {
            indexDailyDao.insertIndexDaily(indexDaily);
        }

        return indexDailyList.size();
    }

    @Override
    public List<String> getAllIndexTsCodes(){
        return indexInfoDao.getAllIndexInfoTsCodes(0, 10000);
    }

    @Override
    public int getIndexCount(){
        return indexInfoDao.getIndexInfoCount();
    }


}
