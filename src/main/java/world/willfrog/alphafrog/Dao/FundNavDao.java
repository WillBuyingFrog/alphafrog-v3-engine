package world.willfrog.alphafrog.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import world.willfrog.alphafrog.Entity.FundNav;

@Mapper
public interface FundNavDao {

  @Insert("insert into alphafrog_fund_nav(ts_code, ann_date, nav_date, unit_nav, accum_nav, net_asset, total_net_asset, adj_nav) " +
          "values(#{tsCode}, #{annDate}, #{navDate}, #{unitNav}, #{accumNav}, #{netAsset}, #{totalNetAsset}, #{adjNav})" +
          "ON CONFLICT do nothing")
  int insertFundNav(FundNav fundNav);





}
