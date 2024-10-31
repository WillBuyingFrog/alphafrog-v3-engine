package world.willfrog.alphafrog.Dao.Fund;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.Fund.FundInfo;

@Mapper
public interface FundInfoDao {

    @Insert("INSERT INTO alphafrog_fund_info (ts_code, name, management, custodian, fund_type, found_date, due_date, " +
            "list_date, issue_date, delist_date, issue_amount, m_fee, c_fee, duration_year, p_value, min_amount, " +
            "exp_return, benchmark, status, invest_type, type, trustee, purc_startdate, redm_startdate, market) " +
            "VALUES (#{tsCode}, #{name}, #{management}, #{custodian}, #{fundType}, #{foundDate}, #{dueDate}, " +
            "#{listDate}, #{issueDate}, #{delistDate}, #{issueAmount}, #{mFee}, #{cFee}, #{durationYear}, #{pValue}, " +
            "#{minAmount}, #{expReturn}, #{benchmark}, #{status}, #{investType}, #{type}, #{trustee}, " +
            "#{purcStartDate}, #{redmStartDate}, #{market}) " +
            "ON CONFLICT (ts_code) DO NOTHING")
    int insertFundInfo(FundInfo fundInfo);

}
