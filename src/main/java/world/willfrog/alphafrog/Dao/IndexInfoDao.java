package world.willfrog.alphafrog.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.IndexInfo;

import java.util.List;

@Mapper
public interface IndexInfoDao {

  @Insert("INSERT INTO alphafrog_index_info (ts_code, name, fullname, market, publisher, index_type, category, base_date, base_point, list_date, weight_rule,\"desc\", exp_date) " +
          "VALUES (#{tsCode}, #{name}, #{fullName}, #{market}, #{publisher}, #{indexType}, #{category}, #{baseDate}, #{basePoint}, #{listDate}, #{weightRule}, #{desc}, #{expDate})" +
          "ON CONFLICT (ts_code) DO NOTHING")
  int insertIndexInfo(IndexInfo indexInfo);

  @Insert("<script>" +
          "INSERT INTO alphafrog_index_info (ts_code, name, fullname, market, publisher, index_type, category, base_date, base_point, list_date, weight_rule,\"desc\", exp_date) " +
          "VALUES " +
          "<foreach collection='indexInfoList' item='indexInfo' separator=','>" +
          "(#{indexInfo.tsCode}, #{indexInfo.name}, #{indexInfo.fullName}, #{indexInfo.market}, #{indexInfo.publisher}, #{indexInfo.indexType}, #{indexInfo.category}, #{indexInfo.baseDate}, #{indexInfo.basePoint}, #{indexInfo.listDate}, #{indexInfo.weightRule}, #{indexInfo.desc}, #{indexInfo.expDate})" +
          "</foreach>" +
          "ON CONFLICT (ts_code) DO NOTHING" +
          "</script>")
  int insertIndexInfos(List<IndexInfo> indexInfoList);


}
