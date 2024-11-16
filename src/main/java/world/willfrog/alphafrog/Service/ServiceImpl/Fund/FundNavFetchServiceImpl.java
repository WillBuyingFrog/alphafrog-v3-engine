package world.willfrog.alphafrog.Service.ServiceImpl.Fund;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;
import world.willfrog.alphafrog.Dao.Fund.FundNavDao;
import world.willfrog.alphafrog.Entity.Fund.FundNav;
import world.willfrog.alphafrog.Service.Fund.FundNavFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class FundNavFetchServiceImpl implements FundNavFetchService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;
    @Autowired
    TuShareRequestUtils tuShareRequestUtils;
    @Autowired
    DateConvertUtils DateConvertUtils;


    /**
     * 输入包含**所有列**的TuShare基金净值原始JSON数据，将其持久化到数据库中
     * 注意：列的排序必须和TuShare官方默认的列排序一致
     * 见 <a href="https://tushare.pro/document/2?doc_id=119">TuShare文档</a> 中的表格
     */
    int storeFundNavsByRawFullTuShareOutput(JSONArray data) {

        List<FundNav> fundNavList = new ArrayList<>();

        try {
            for (int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                FundNav fundNav = new FundNav();
                fundNav.setTsCode(item.getString(0));

                String annDateStr = item.getString(1);
                fundNav.setAnnDate(annDateStr == null ? null : DateConvertUtils.convertDateStrToLong(annDateStr, "yyyyMMdd"));

                String navDateStr = item.getString(2);
                fundNav.setNavDate(navDateStr == null ? null : DateConvertUtils.convertDateStrToLong(navDateStr, "yyyyMMdd"));

                fundNav.setUnitNav(item.getDouble(3));
                fundNav.setAccumNav(item.getDouble(4));
                fundNav.setAccumDiv(item.getDouble(5));
                fundNav.setNetAsset(item.getDouble(6));
                fundNav.setTotalNetAsset(item.getDouble(7));
                fundNav.setAdjNav(item.getDouble(8));
                fundNavList.add(fundNav);
            }

            // 批量插入数据的写法，详见
            // https://github.com/mybatis/mybatis-3/wiki/FAQ#how-do-i-code-a-batch-insert
            try ( SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH) ) {
                FundNavDao fundNavDao = sqlSession.getMapper(FundNavDao.class);
                for (FundNav fundNav : fundNavList) {
                    fundNavDao.insertFundNav(fundNav);
                }
                sqlSession.commit();
            } catch (Exception e) {
                System.out.println("Error occurred while inserting fund nav data");
                e.printStackTrace();
                return -2;
            }

        } catch (Exception e) {
            System.out.println("Error occurred while converting fund nav raw data");
            e.printStackTrace();
            return -1;
        }
        return fundNavList.size();
    }

    /**
     * 爬取单个基金净值的函数
     * 根据给定的基金代码和起止日期，爬取该基金在此时间段内的所有净值数据
     *
     * @param tsCode    基金代码
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @param offset    分页偏移量
     * @param limit     分页最多条数
     * @return 若成功，则返回插入的净值数据条数；若失败，则返回负数
     */
    @Override
    public int directFetchFundNavByTsCodeAndDateRange(String tsCode, String startDate, String endDate, int offset, int limit) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        // 构建请求参数组合
        params.put("api_name", "fund_nav");
        queryParams.put("ts_code", tsCode);
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        // 所有条目都要爬取
        params.put("fields", "ts_code,ann_date,nav_date,unit_nav,accum_nav,accum_div,net_asset,total_netasset,adj_nav");

        JSONObject res = tuShareRequestUtils.createTusharePostRequest(params);

        if (res == null) {
            return -2;
        }

        JSONArray fields = res.getJSONObject("data").getJSONArray("fields");
        JSONArray data = res.getJSONObject("data").getJSONArray("items");

        // 插入所有爬取到的基金净值
        return storeFundNavsByRawFullTuShareOutput(data);

    }

    /**
     * 单次爬取大量基金净值的函数，不建议频繁调用
     * 根据给定场内/场外市场以及净值公布日期，爬取所有符合条件的基金净值
     *
     * @param navDate 净值公布日期
     * @param market  场内/场外市场。场内为E，场外为O
     * @param offset  分页偏移量
     * @param limit   分页最多条数
     * @return 若成功，则返回插入的净值数据条数；若失败，则返回负数
     */
    @Override
    public int directFetchFundNavByNavDateAndMarket(String navDate, String market, int offset, int limit) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        // 构建请求参数组合
        params.put("api_name", "fund_nav");
        queryParams.put("nav_date", navDate);
        if (market != null) {
            queryParams.put("market", market);
        }
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        // 所有条目都要爬取
        params.put("fields", "ts_code,ann_date,nav_date,unit_nav,accum_nav,accum_div,net_asset,total_netasset,adj_nav");

        boolean hasMore = true;
        int queriedRows = 0;
        while(hasMore){
            JSONObject res = tuShareRequestUtils.createTusharePostRequest(params);

            if (res == null) {
                return -2;
            }

            JSONArray fields = res.getJSONObject("data").getJSONArray("fields");
            JSONArray data = res.getJSONObject("data").getJSONArray("items");
            hasMore = res.getJSONObject("data").getBoolean("has_more");
            queriedRows += storeFundNavsByRawFullTuShareOutput(data);
            if(hasMore) {
                offset += limit;
                queryParams.put("offset", offset);
            }
        }

        // 插入所有爬取到的基金净值
        return queriedRows;
    }
}
