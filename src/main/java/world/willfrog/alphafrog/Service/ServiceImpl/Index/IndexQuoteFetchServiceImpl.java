package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;
import world.willfrog.alphafrog.Dao.Index.IndexDailyDao;
import world.willfrog.alphafrog.Entity.Common.Quote;
import world.willfrog.alphafrog.Entity.Index.IndexDaily;
import world.willfrog.alphafrog.Service.Index.IndexQuoteFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexQuoteFetchServiceImpl implements IndexQuoteFetchService {

    TuShareRequestUtils tuShareRequestUtils;

    DateConvertUtils dateConvertUtils;

    SqlSessionFactory sqlSessionFactory;

    IndexDailyDao indexDailyDao;

    public IndexQuoteFetchServiceImpl(TuShareRequestUtils tuShareRequestUtils,
                                      DateConvertUtils dateConvertUtils,
                                      SqlSessionFactory sqlSessionFactory,
                                      IndexDailyDao indexDailyDao) {
        this.dateConvertUtils = dateConvertUtils;
        this.tuShareRequestUtils = tuShareRequestUtils;
        this.sqlSessionFactory = sqlSessionFactory;
        this.indexDailyDao = indexDailyDao;
    }


    List<Quote> convertRawFullTuShareDataToQuoteList(JSONArray data) {
        List<Quote> ret = new ArrayList<>();

        for(int i = 0; i < data.size(); i++){
            JSONArray item = data.getJSONArray(i);
            Quote quote = new Quote();
            quote.setTsCode(item.getString(0));

            String tradeDateStr = item.getString(1);
            quote.setTradeDate(dateConvertUtils.convertDateStrToLong(tradeDateStr, "yyyyMMdd"));

            quote.setOpen(item.getDouble(2));
            quote.setHigh(item.getDouble(3));
            quote.setLow(item.getDouble(4));
            quote.setClose(item.getDouble(5));
            quote.setPreClose(item.getDouble(6));
            quote.setChange(item.getDouble(7));
            quote.setPctChg(item.getDouble(8));
            quote.setVol(item.getDouble(9));
            quote.setAmount(item.getDouble(10));
            ret.add(quote);
        }

        return ret;
    }

    int storeIndexDailies(List<IndexDaily> indexDailyList) {
        try ( SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)){
            for (IndexDaily indexDaily : indexDailyList) {
                indexDailyDao.insertIndexDaily(indexDaily);
            }
            sqlSession.commit();
        } catch (Exception e) {
            System.out.println("Error occurred while storing index daily data" );
            e.printStackTrace();
        }

        return indexDailyList.size();
    }

    @Override
    public int directFetchIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                   int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_daily");
        if(tsCode != null) {
            queryParams.put("ts_code", tsCode);
        }
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        params.put("fields", "ts_code,trade_date,open,high,low,close,pre_close,change,pct_chg,vol,amount");

        JSONObject ret = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray fields = ret.getJSONObject("data").getJSONArray("fields");
        JSONArray data = ret.getJSONObject("data").getJSONArray("items");

        try {
            List<Quote> quoteList = convertRawFullTuShareDataToQuoteList(data);

            List<IndexDaily> indexDailyList = new ArrayList<>();
            for(Quote quote : quoteList){
                IndexDaily indexDaily = new IndexDaily();
                BeanUtils.copyProperties(quote, indexDaily);
                indexDailyList.add(indexDaily);
            }
            return storeIndexDailies(indexDailyList);
        } catch (Exception e) {
            System.out.println("Error occurred while converting raw data to IndexDaily list");
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int directFetchIndexDailyByTradeDate(String tradeDate, int offset, int limit){
        
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_daily");
        queryParams.put("start_date", tradeDate);
        queryParams.put("end_date", tradeDate);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        params.put("fields", "ts_code,trade_date,open,high,low,close,pre_close,change,pct_chg,vol,amount");

        JSONObject ret = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray fields = ret.getJSONObject("data").getJSONArray("fields");
        JSONArray data = ret.getJSONObject("data").getJSONArray("items");

        try {
            List<Quote> quoteList = convertRawFullTuShareDataToQuoteList(data);

            List<IndexDaily> indexDailyList = new ArrayList<>();
            for(Quote quote : quoteList){
                IndexDaily indexDaily = new IndexDaily();
                BeanUtils.copyProperties(quote, indexDaily);
                indexDailyList.add(indexDaily);
            }
            return storeIndexDailies(indexDailyList);

        } catch (Exception e) {
            System.out.println("Error occurred while converting raw data to IndexDaily list");
            e.printStackTrace();
        }

        return -1;
    }

    public int directFetchIndexWeeklyByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                   int offset, int limit) {
        return 0;
    }

}
