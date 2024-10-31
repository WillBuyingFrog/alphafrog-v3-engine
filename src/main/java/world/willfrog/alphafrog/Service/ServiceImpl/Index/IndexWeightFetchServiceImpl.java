package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;
import world.willfrog.alphafrog.Dao.Index.IndexWeightDao;
import world.willfrog.alphafrog.Entity.Index.IndexWeight;
import world.willfrog.alphafrog.Service.Index.IndexWeightFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexWeightFetchServiceImpl implements IndexWeightFetchService {


    TuShareRequestUtils tuShareRequestUtils;

    DateConvertUtils dateConvertUtils;

    IndexWeightDao indexWeightDao;

    SqlSessionFactory sqlSessionFactory;

    public IndexWeightFetchServiceImpl (TuShareRequestUtils tuShareRequestUtils,
                                        DateConvertUtils dateConvertUtils,
                                        IndexWeightDao indexWeightDao,
                                        SqlSessionFactory sqlSessionFactory) {
        this.tuShareRequestUtils = tuShareRequestUtils;
        this.dateConvertUtils = dateConvertUtils;
        this.indexWeightDao = indexWeightDao;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    int storeIndexWeightByRawFullTuShareOutput(JSONArray data) {

        try {

            List<IndexWeight> indexWeightList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                JSONArray indexWeightRaw = data.getJSONArray(i);
                // Convert raw data to IndexWeight Entity
                IndexWeight indexWeight = new IndexWeight();
                indexWeight.setIndexCode(indexWeightRaw.getString(0));
                indexWeight.setConCode(indexWeightRaw.getString(1));

                String tradeDateStr = indexWeightRaw.getString(2);
                Long tradeDate = dateConvertUtils.convertDateStrToLong(tradeDateStr, "yyyyMMdd");
                indexWeight.setTradeDate(tradeDate);

                indexWeight.setWeight(indexWeightRaw.getDouble(3));

                indexWeightList.add(indexWeight);

            }

            try ( SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH) ) {
                IndexWeightDao indexWeightDao = sqlSession.getMapper(IndexWeightDao.class);
                for (IndexWeight indexWeight : indexWeightList) {
                    indexWeightDao.insertIndexWeight(indexWeight);
                }
                sqlSession.commit();
                return indexWeightList.size();
            } catch (Exception e) {
                System.out.println("Error occurred while inserting IndexWeight data into the database.");
                e.printStackTrace();
                return -2;
            }

        } catch (Exception e ) {
            System.out.println("Error occurred while converting raw data to IndexWeight Entity.");
            e.printStackTrace();
            return -1;
        }

    }


    @Override
    public int directFetchIndexWeightByIndexCodeAndDateRange(String indexCode, String startDate, String endDate,
                                                             int offset, int limit) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_weight");

        if (indexCode != null) {
           queryParams.put("index_code", indexCode);
        }
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);

        params.put("params", queryParams);
        params.put("fields", "index_code,con_code,trade_date,weight");

        JSONObject data = tuShareRequestUtils.createTusharePostRequest(params);

        
        JSONArray fields = data.getJSONObject("data").getJSONArray("fields");
        JSONArray items = data.getJSONObject("data").getJSONArray("items");

        return storeIndexWeightByRawFullTuShareOutput(items);
    }

    @Override
    public int directFetchIndexWeightByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                         int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "index_weight");
        queryParams.put("ts_code", tsCode);
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);

        params.put("params", queryParams);
        params.put("fields", "index_code,con_code,trade_date,weight");

        JSONObject data = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray fields = data.getJSONObject("data").getJSONArray("fields");
        JSONArray items = data.getJSONObject("data").getJSONArray("items");

        return storeIndexWeightByRawFullTuShareOutput(items);
    }
}
