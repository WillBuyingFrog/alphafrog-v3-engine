package world.willfrog.alphafrog.Dao.Index;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.Index.IndexWeight;

@Mapper
public interface IndexWeightDao {

    @Insert("INSERT INTO alphafrog_index_weight (index_code, con_code, trade_date, weight) " +
            "VALUES (#{indexCode}, #{conCode}, #{tradeDate}, #{weight}) " +
            "ON CONFLICT (index_code, con_code, trade_date) DO NOTHING")
    int insertIndexWeight(IndexWeight indexWeight);

}
