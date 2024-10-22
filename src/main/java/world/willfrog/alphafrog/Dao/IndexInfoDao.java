package world.willfrog.alphafrog.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.IndexInfo;

@Mapper
public interface IndexInfoDao {

  @Insert("INSERT INTO alphafrog_index_info (ts_code, name, fullname, market, publisher, index_type, category, base_date, base_point, list_date, weight_rule,\"desc\", exp_date) " +
          "VALUES (#{tsCode}, #{name}, #{fullName}, #{market}, #{publisher}, #{indexType}, #{category}, #{baseDate}, #{basePoint}, #{listDate}, #{weightRule}, #{desc}, #{expDate})" +
          "ON CONFLICT (ts_code) DO NOTHING")
  int insertIndexInfo(IndexInfo indexInfo);


}
