package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import world.willfrog.alphafrog.Dao.Index.IndexInfoDao;
import world.willfrog.alphafrog.Entity.Index.IndexDaily;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Entity.Index.IndexWeight;
import world.willfrog.alphafrog.Service.Index.IndexInformationUserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IndexInformationUserServiceImpl implements IndexInformationUserService {

    IndexInfoDao indexInfoDao;

    public IndexInformationUserServiceImpl(IndexInfoDao indexInfoDao) {
        this.indexInfoDao = indexInfoDao;
    }



    @Override
    public List<IndexInfo> getIndexInfoByTsCode(String tsCode) {
        // Implementation logic to fetch index information by tsCode
        List<IndexInfo> indexInfoList = new ArrayList<>();

        try {
            indexInfoList.addAll(indexInfoDao.getIndexInfoByTsCode(tsCode));
        } catch (Exception e) {
            log.error("Error occurred while querying IndexInfo by ts_code");
            log.error("Stack trace info", e);
        }

        return indexInfoList;
    }

    @Override
    public List<IndexInfo> getIndexInfoByNames(String queryName) {

        List<IndexInfo> indexInfoList = new ArrayList<>();

        try {
            List<IndexInfo> queryFullNameResult = indexInfoDao.getIndexInfoByFullName(queryName);
            List<IndexInfo> queryNameResult = indexInfoDao.getIndexInfoByName(queryName);
            indexInfoList.addAll(queryNameResult);
            indexInfoList.addAll(queryFullNameResult);
        } catch (Exception e){
            log.error("Error occurred while querying IndexInfo by names");
            log.error("Stack trace info", e);
        }
        


        return indexInfoList;
    }

    @Override
    public List<IndexDaily> getIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate) {

        List<IndexDaily> indexDailyList = new ArrayList<>();

        return indexDailyList;
    }

    @Override
    public List<IndexWeight> getIndexWeightByTsCodeAndDateRange(String tsCode, String startDate, String endDate) {
        List<IndexWeight> indexWeightList = new ArrayList<>();

        return indexWeightList;
    }
}
