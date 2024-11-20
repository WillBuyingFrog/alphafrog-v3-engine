package world.willfrog.alphafrog.Dao.Index;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import world.willfrog.alphafrog.Entity.Index.IndexDaily;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Entity.Index.IndexWeight;

import java.util.List;

@Mapper
public interface IndexInfoDao {

    @Insert("INSERT INTO alphafrog_index_info (ts_code, name, fullname, market, publisher, index_type, category, base_date, base_point, list_date, weight_rule,\"desc\", exp_date) " +
            "VALUES (#{tsCode}, #{name}, #{fullName}, #{market}, #{publisher}, #{indexType}, #{category}, #{baseDate}, #{basePoint}, #{listDate}, #{weightRule}, #{desc}, #{expDate})" +
            "ON CONFLICT (ts_code) DO NOTHING")
    int insertIndexInfo(IndexInfo indexInfo);

    @Select("SELECT * FROM alphafrog_index_info WHERE ts_code like '%${tsCode}%'")
    List<IndexInfo> getIndexInfoByTsCode(String tsCode);

    @Select("SELECT * FROM alphafrog_index_info WHERE fullname like '%${fullName}%'")
    List<IndexInfo> getIndexInfoByFullName(String fullName);

    @Select("SELECT * FROM alphafrog_index_info WHERE name like '%${name}%'")
    List<IndexInfo> getIndexInfoByName(String name);

    @Select("SELECT count(*) FROM alphafrog_index_info")
    int getIndexInfoCount();

    @Select("SELECT (ts_code) from alphafrog_index_info limit ${limit} offset ${offset}")
    List<String> getAllIndexInfoTsCodes(int offset, int limit);

    List<IndexDaily> getIndexDailyByTsCodeAndDateRange(String tsCode, Long startDate, Long endDate);

    List<IndexWeight> getIndexWeightByTsCodeAndDateRange(String tsCode, Long startDate, Long endDate);


}
