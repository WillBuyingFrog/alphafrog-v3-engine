package world.willfrog.alphafrog.Service.ServiceImpl.Stock;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;
import world.willfrog.alphafrog.Service.Stock.StockInformationFetchService;
import world.willfrog.alphafrog.Entity.Stock.StockInfo;
import world.willfrog.alphafrog.Dao.Stock.StockInfoDao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


@Service
@Slf4j
public class StockInformationFetchServiceImpl implements StockInformationFetchService {

    private final SqlSessionFactory sqlSessionFactory;

    private final DateConvertUtils dateConvertUtils;

    private final TuShareRequestUtils tuShareRequestUtils;

    public StockInformationFetchServiceImpl(SqlSessionFactory sqlSessionFactory,
                                            DateConvertUtils dateConvertUtils,
                                            TuShareRequestUtils tuShareRequestUtils) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.dateConvertUtils = dateConvertUtils;
        this.tuShareRequestUtils = tuShareRequestUtils;
    }

    int storeStockInfoByRawFullTuShareRequest(JSONArray data) {
        List<StockInfo> stockInfoList = new ArrayList<>();

        try {
            for (int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                StockInfo stockInfo = new StockInfo();

                stockInfo.setTsCode(item.getString(0));
                stockInfo.setSymbol(item.getString(1));
                stockInfo.setName(item.getString(2));
                stockInfo.setArea(item.getString(3));
                stockInfo.setIndustry(item.getString(4));
                stockInfo.setFullName(item.getString(5));
                stockInfo.setEnName(item.getString(6));
                stockInfo.setCnSpell(item.getString(7));
                stockInfo.setMarket(item.getString(8));
                stockInfo.setExchange(item.getString(9));
                stockInfo.setCurrType(item.getString(10));
                stockInfo.setListStatus(item.getString(11));

                String listDateStr = item.getString(12);
                stockInfo.setListDate(listDateStr == null ? null : dateConvertUtils.convertDateStrToLong(listDateStr, "yyyyMMdd"));

                String deListDateStr = item.getString(13);
                stockInfo.setDeListDate(deListDateStr == null ? null : dateConvertUtils.convertDateStrToLong(deListDateStr, "yyyyMMdd"));

                stockInfo.setIsHs(item.getString(14));
                stockInfo.setActName(item.getString(15));
                stockInfo.setActEntType(item.getString(16));

                stockInfoList.add(stockInfo);

            }
        } catch (Exception e) {
            log.error("Error occurred while converting raw json data to StockInfo list", e);
            return -1;
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            StockInfoDao stockInfoDao = sqlSession.getMapper(StockInfoDao.class);
            for (StockInfo stockInfo : stockInfoList) {
                stockInfoDao.insertStockInfo(stockInfo);
            }
            sqlSession.commit();
        } catch (Exception e) {
            log.error("Error occurred while inserting stock info data", e);
            return -2;
        }

        return stockInfoList.size();

    }

    @Override
    public int directFetchStockInfoByMarket(String exchange) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "stock_basic");
        queryParams.put("exchange", exchange);
        params.put("params", queryParams);
        params.put("fields", "ts_code,symbol,name,area,industry,fullname,enname,cnspell,market,exchange,curr_type,list_status,list_date,delist_date,is_hs,act_name,act_ent_type");

        JSONObject ret = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray data = ret.getJSONObject("data").getJSONArray("items");

        return storeStockInfoByRawFullTuShareRequest(data);
    }
}
