package world.willfrog.alphafrog.Service.ServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TushareRequestUtils;
import world.willfrog.alphafrog.Dao.FundNavDao;
import world.willfrog.alphafrog.Entity.FundNav;
import world.willfrog.alphafrog.Service.FundFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class FundFetchServiceImpl implements FundFetchService {

  @Autowired
  SqlSessionFactory sqlSessionFactory;
  @Autowired
  TushareRequestUtils tushareRequestUtils;
  @Autowired
  DateConvertUtils DateConvertUtils;


  /**
   * 输入包含**所有列**的TuShare原始JSON数据，将其持久化到数据库中
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
        fundNav.setNetAsset(item.getDouble(5));
        fundNav.setTotalNetAsset(item.getDouble(6));
        fundNav.setAdjNav(item.getDouble(7));
        fundNavList.add(fundNav);
      }

      // 批量插入数据的写法，详见
      // https://github.com/mybatis/mybatis-3/wiki/FAQ#how-do-i-code-a-batch-insert
      try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
        FundNavDao fundNavDao = sqlSession.getMapper(FundNavDao.class);
        for (FundNav fundNav : fundNavList) {
          fundNavDao.insertFundNav(fundNav);
        }
        sqlSession.commit();
      } catch (Exception e) {
        System.out.println("Error occured while inserting fund nav data");
        e.printStackTrace();
        return -2;
      }

    } catch (Exception e){
      System.out.println("Error occured while converting fund nav raw data");
      e.printStackTrace();
      return -1;
    }
    return fundNavList.size();
  }

  @Override
  public int directFetchFundNavByTsCodeAndDateRange(String tsCode, String startDate, String endDate, int offset, int limit) {

    Map<String, Object> params = new HashMap<>();
    Map<String, Object> queryParams = new HashMap<>();

    params.put("api_name", "fund_nav");
    queryParams.put("ts_code", tsCode);
    queryParams.put("start_date", startDate);
    queryParams.put("end_date", endDate);
    queryParams.put("offset", offset);
    queryParams.put("limit", limit);
    params.put("params", queryParams);
    params.put("fields", "ts_code,ann_date,nav_date,unit_nav,accum_nav,net_asset,total_netasset,adj_nav");

    JSONObject res = tushareRequestUtils.createTusharePostRequest(params);

    if(res == null){
      return -2;
    }

    JSONArray fields = res.getJSONObject("data").getJSONArray("fields");
    JSONArray data = res.getJSONObject("data").getJSONArray("items");

    // 插入所有爬取到的基金净值
    return storeFundNavsByRawFullTuShareOutput(data);

  }

  @Override
  public int directFetchFundNavByNavDateAndMarket(String navDate, String Market, int offset, int limit) {
    return 0;
  }
}
