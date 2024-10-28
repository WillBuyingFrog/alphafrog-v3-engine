package world.willfrog.alphafrog.Service.ServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TushareRequestUtils;
import world.willfrog.alphafrog.Dao.FundPortfolioDao;
import world.willfrog.alphafrog.Service.FundPortfolioFetchService;
import world.willfrog.alphafrog.Entity.FundPortfolio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FundPortfolioFetchServiceImpl implements FundPortfolioFetchService {

  SqlSessionFactory sqlSessionFactory;

  TushareRequestUtils tushareRequestUtils;

  DateConvertUtils dateConvertUtils;


  public FundPortfolioFetchServiceImpl(SqlSessionFactory sqlSessionFactory,
                                        TushareRequestUtils tushareRequestUtils,
                                        DateConvertUtils dateConvertUtils) {
    this.sqlSessionFactory = sqlSessionFactory;
    this.tushareRequestUtils = tushareRequestUtils;
    this.dateConvertUtils = dateConvertUtils;
  }

  int storeFundPortfoliosByRawTuShareOutput(JSONArray data) {
    List<FundPortfolio> fundPortfolioList = new ArrayList<>();

    try{
      for(int i = 0; i < data.size(); i++) {
        JSONArray item = data.getJSONArray(i);
        FundPortfolio fundPortfolio = new FundPortfolio();
        fundPortfolio.setTsCode(item.getString(0));

        String annDateStr = item.getString(1);
        fundPortfolio.setAnnDate(annDateStr == null ? null : dateConvertUtils.convertDateStrToLong(annDateStr, "yyyyMMdd"));

        String endDateStr = item.getString(2);
        fundPortfolio.setEndDate(endDateStr == null ? null : dateConvertUtils.convertDateStrToLong(endDateStr, "yyyyMMdd"));

        fundPortfolio.setSymbol(item.getString(3));
        fundPortfolio.setMkv(item.getDouble(4));
        fundPortfolio.setAmount(item.getDouble(5));
        fundPortfolio.setStkMkvRatio(item.getDouble(6));
        fundPortfolio.setStkFloatRatio(item.getDouble(7));

        fundPortfolioList.add(fundPortfolio);
      }

      try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
        FundPortfolioDao fundPortfolioDao = sqlSession.getMapper(FundPortfolioDao.class);
        for (FundPortfolio fundPortfolio : fundPortfolioList) {
          fundPortfolioDao.insertFundPortfolio(fundPortfolio);
        }
        sqlSession.commit();
      } catch (Exception e) {
        System.out.println("Error occured while inserting fund portfolio data");
        e.printStackTrace();
        return -2;
      }
    } catch (Exception e) {
      System.out.println("Error occured while converting fund portfolio raw data");
      e.printStackTrace();
      return -1; // Indicate failure
    }

    return fundPortfolioList.size();
  }

  @Override
  public int directFetchFundPortfolioByTsCodeAndDateRange(String tsCode, String startDate, String endDate,
                                                          int offset, int limit) {
    // Implementation logic here

    Map<String, Object> params = new HashMap<>();
    Map<String, Object> queryParams = new HashMap<>();

    params.put("api_name", "fund_portfolio");
    if(tsCode != null) {
      queryParams.put("ts_code", tsCode);
    }
    queryParams.put("start_date", startDate);
    queryParams.put("end_date", endDate);
    queryParams.put("offset", offset);
    queryParams.put("limit", limit);
    params.put("params", queryParams);
    params.put("fields", "ts_code,ann_date,end_date,symbol,mkv,amount,stk_mkv_ratio,stk_float_ratio");

    JSONObject ret = tushareRequestUtils.createTusharePostRequest(params);

    JSONArray fields = ret.getJSONObject("data").getJSONArray("fields");
    JSONArray data = ret.getJSONObject("data").getJSONArray("items");


    return storeFundPortfoliosByRawTuShareOutput(data); // Placeholder return value
  }
}

