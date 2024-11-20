package world.willfrog.alphafrog.Dao.Stock;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import world.willfrog.alphafrog.Entity.Stock.StockInfo;

import java.util.List;

@Mapper
public interface StockInfoDao {

    @Insert("INSERT INTO alphafrog_stock_info (ts_code, symbol, name, area, industry, fullname, enname, market, exchange, curr_type, list_status, list_date, delist_date, is_hs, act_name, act_ent_type) " +
            "VALUES (#{tsCode}, #{symbol}, #{name}, #{area}, #{industry}, #{fullName}, #{enName}, #{market}, #{exchange}, #{currType}, #{listStatus}, #{listDate}, #{deListDate}, #{isHs}, #{actName}, #{actEntType})" +
            "ON CONFLICT (ts_code, symbol) DO NOTHING")
    int insertStockInfo(StockInfo stockInfo);

    @Select("SELECT * FROM alphafrog_stock_info WHERE ts_code like '%${tsCode}%'")
    List<StockInfo> getStockInfosByTsCode(String tsCode);

    @Select("SELECT * FROM alphafrog_stock_info WHERE name like '%${name}%'")
    List<StockInfo> getStockInfosByName(String name);

    @Select("SELECT * FROM alphafrog_stock_info WHERE fullname like '%${fullName}%'")
    List<StockInfo> getStockInfosByFullName(String fullName);
}
