package world.willfrog.alphafrog.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import world.willfrog.alphafrog.Entity.IndexDaily;

@Mapper
public interface IndexDailyDao {

  @Insert("INSERT INTO alphafrog_index_daily (ts_code, trade_date, close, open, high, low, pre_close, change, pct_chg, vol, amount) " +
          "VALUES (#{tsCode}, #{tradeDate}, #{close}, #{open}, #{high}, #{low}, #{preClose}, #{change}, #{pctChg}, #{vol}, #{amount})" + 
          "ON CONFLICT (ts_code, trade_date) DO NOTHING")
  int insertIndexDaily(IndexDaily indexDaily);

  @Select("SELECT * FROM alphafrog_index_daily WHERE ts_code = #{tsCode} AND trade_date BETWEEN #{startDate} AND #{endDate}")
  List<IndexDaily> getIndexDailiesByTsCodeAndDateRange(String tsCode, Long startDate, Long endDate);

}
