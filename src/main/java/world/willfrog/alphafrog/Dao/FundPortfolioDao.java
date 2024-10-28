package world.willfrog.alphafrog.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.FundPortfolio;

@Mapper
public interface FundPortfolioDao {

  @Insert("INSERT INTO alphafrog_fund_portfolio (ts_code, ann_date, end_date, symbol, mkv, amount, stk_mkv_ratio, stk_float_ratio) " +
          "VALUES (#{tsCode}, #{annDate}, #{endDate}, #{symbol}, #{mkv}, #{amount}, #{stkMkvRatio}, #{stkFloatRatio}) " +
          "ON CONFLICT (ts_code, symbol, ann_date) DO NOTHING")
  int insertFundPortfolio(FundPortfolio  fundPortfolio);

}
