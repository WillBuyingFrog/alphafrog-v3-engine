package world.willfrog.alphafrog.Service.ServiceImpl.Fund;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Common.DateConvertUtils;
import world.willfrog.alphafrog.Common.TuShareRequestUtils;
import world.willfrog.alphafrog.Dao.Fund.FundInfoDao;
import world.willfrog.alphafrog.Entity.Fund.FundInfo;
import world.willfrog.alphafrog.Service.Fund.FundInfoFetchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FundInfoFetchServiceImpl implements FundInfoFetchService {


    SqlSessionFactory sqlSessionFactory;

    DateConvertUtils dateConvertUtils;

    TuShareRequestUtils tuShareRequestUtils;


    public FundInfoFetchServiceImpl(DateConvertUtils dateConvertUtils,
                                     TuShareRequestUtils tushareRequestUtils,
                                     SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.dateConvertUtils = dateConvertUtils;
        this.tuShareRequestUtils = tushareRequestUtils;
    }

    int storeFundInfosByRawTuShareOutput(JSONArray data) {
        List<FundInfo> fundInfoList = new ArrayList<>();

        int affectedRows = 0;

        try{
            for(int i = 0; i < data.size(); i++) {
                JSONArray item = data.getJSONArray(i);
                FundInfo fundInfo = new FundInfo();
                fundInfo.setTsCode(item.getString(0));
                fundInfo.setName(item.getString(1));
                fundInfo.setManagement(item.getString(2));
                fundInfo.setCustodian(item.getString(3));
                fundInfo.setFundType(item.getString(4));

                String fundDateStr = item.getString(5);
                fundInfo.setFoundDate(fundDateStr == null ? null : dateConvertUtils.convertDateStrToLong(fundDateStr, "yyyyMMdd"));

                String dueDateStr = item.getString(6);
                fundInfo.setDueDate(dueDateStr == null ? null : dateConvertUtils.convertDateStrToLong(dueDateStr, "yyyyMMdd"));

                String listDateStr = item.getString(7);
                fundInfo.setListDate(listDateStr == null ? null : dateConvertUtils.convertDateStrToLong(listDateStr, "yyyyMMdd"));

                String issueDateStr = item.getString(8);
                fundInfo.setIssueDate(issueDateStr == null ? null : dateConvertUtils.convertDateStrToLong(issueDateStr, "yyyyMMdd"));

                String delistDateStr = item.getString(9);
                fundInfo.setDelistDate(delistDateStr == null ? null : dateConvertUtils.convertDateStrToLong(delistDateStr, "yyyyMMdd"));

                fundInfo.setIssueAmount(item.getDouble(10));
                fundInfo.setMFee(item.getDouble(11));
                fundInfo.setCFee(item.getDouble(12));
                fundInfo.setDurationYear(item.getDouble(13));
                fundInfo.setPValue(item.getDouble(14));
                fundInfo.setMinAmount(item.getDouble(15));
                fundInfo.setExpReturn(item.getDouble(16));
                fundInfo.setBenchmark(item.getString(17));
                fundInfo.setStatus(item.getString(18));
                fundInfo.setInvestType(item.getString(19));
                fundInfo.setType(item.getString(20));
                fundInfo.setTrustee(item.getString(21));

                String purcStartDate = item.getString(22);
                fundInfo.setPurcStartDate(purcStartDate == null ? null : dateConvertUtils.convertDateStrToLong(purcStartDate, "yyyyMMdd"));

                String redmStartDate = item.getString(23);
                fundInfo.setRedmStartDate(redmStartDate == null ? null : dateConvertUtils.convertDateStrToLong(redmStartDate, "yyyyMMdd"));

                fundInfo.setMarket(item.getString(24));
                fundInfoList.add(fundInfo);
            }

            try ( SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
                FundInfoDao fundInfoDao = sqlSession.getMapper(FundInfoDao.class);
                for (FundInfo fundInfo : fundInfoList) {
                    affectedRows += fundInfoDao.insertFundInfo(fundInfo);
                }
                sqlSession.commit();
            } catch (Exception e) {
                System.out.println("Error occurred while inserting fund info data");
                e.printStackTrace();
                return -2;
            }

        } catch (Exception e) {
            System.out.println("Error occurred while converting fund info raw data");
            e.printStackTrace();
            return -1;
        }

        return affectedRows;
    }

    @Override
    public int directFetchFundInfoByMarket(String market, int offset, int limit) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "fund_basic");
        queryParams.put("market", market);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        params.put("fields", "ts_code,name,management,custodian,fund_type,found_date,due_date,list_date,issue_date," +
                "delist_date,issue_amount,m_fee,c_fee,duration_year,p_value,min_amount,exp_return,benchmark,status," +
                "invest_type,type,trustee,purc_startdate,redm_startdate,market");

        JSONObject ret = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray fields = ret.getJSONObject("data").getJSONArray("fields");
        JSONArray data = ret.getJSONObject("data").getJSONArray("items");

        return storeFundInfosByRawTuShareOutput(data);
    }

    @Override
    public int directFetchFundInfoByTsCode(String tsCode, int offset, int limit) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> queryParams = new HashMap<>();

        params.put("api_name", "fund_basic");
        queryParams.put("ts_code", tsCode);
        queryParams.put("offset", offset);
        queryParams.put("limit", limit);
        params.put("params", queryParams);
        params.put("fields", "ts_code,name,management,custodian,fund_type,found_date,due_date,list_date,issue_date," +
                "delist_date,issue_amount,m_fee,c_fee,duration_year,p_value,min_amount,exp_return,benchmark,status," +
                "invest_type,type,trustee,purc_startdate,redm_startdate,market");

        JSONObject ret = tuShareRequestUtils.createTusharePostRequest(params);

        JSONArray fields = ret.getJSONObject("data").getJSONArray("fields");
        JSONArray data = ret.getJSONObject("data").getJSONArray("items");

        return storeFundInfosByRawTuShareOutput(data);
    }
}
