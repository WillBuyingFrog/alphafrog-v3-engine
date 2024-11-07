package world.willfrog.alphafrog.Service.ServiceImpl.Index;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import world.willfrog.alphafrog.Dao.Index.IndexInfoDao;
import world.willfrog.alphafrog.Dao.Index.IndexDailyDao;
import world.willfrog.alphafrog.Dao.Index.IndexWeightDao;
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
    IndexDailyDao indexDailyDao;
    IndexWeightDao indexWeightDao;

    public IndexInformationUserServiceImpl(IndexInfoDao indexInfoDao, IndexDailyDao indexDailyDao, IndexWeightDao indexWeightDao) {
        this.indexInfoDao = indexInfoDao;
        this.indexDailyDao = indexDailyDao;
        this.indexWeightDao = indexWeightDao;
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
    public List<IndexInfo> searchIndexInfo(String queryStr, int page, int pageSize) {
        List<IndexInfo> indexInfoList = new ArrayList<>();

        List<IndexInfo> indexInfoRetList = new ArrayList<>();
        try {
            List<IndexInfo> queryTsCodeResult = indexInfoDao.getIndexInfoByTsCode(queryStr);
            List<IndexInfo> queryFullNameResult = indexInfoDao.getIndexInfoByFullName(queryStr);
            List<IndexInfo> queryNameResult = indexInfoDao.getIndexInfoByName(queryStr);
            indexInfoList.addAll(queryTsCodeResult);
            indexInfoList.addAll(queryFullNameResult);
            indexInfoList.addAll(queryNameResult);

            // 查询内容页数超过所有记录条数，返回空记录
            if(indexInfoList.size() <= (page - 1) * pageSize) {
                return new ArrayList<>();
            }

            for(int i = (page - 1) * pageSize; i < page * pageSize && i < indexInfoList.size(); i++) {
                indexInfoRetList.add(indexInfoList.get(i));
            }
        } catch (Exception e) {
            log.error("Error occurred while querying IndexInfo by search string");
            log.error("Stack trace info", e);
        }

        return indexInfoRetList;
    }

    @Override
    public List<IndexDaily> getIndexDailyByTsCodeAndDateRange(String tsCode, long startDate, long endDate) {

        List<IndexDaily> indexDailyList = new ArrayList<>();

        try {
            indexDailyList.addAll(indexDailyDao.getIndexDailiesByTsCodeAndDateRange(tsCode, startDate, endDate));

        } catch (Exception e) {
            log.error("Error occurred while querying IndexDaily by ts_code and date range");
            log.error("Stack trace info", e);
        }

        return indexDailyList;
    }

    @Override
    public List<IndexWeight> getIndexWeightByTsCodeAndDateRange(String tsCode, long startDate, long endDate) {
        List<IndexWeight> indexWeightList = new ArrayList<>();

        try {
            indexWeightList.addAll(indexWeightDao.getIndexWeightsByTsCodeAndDateRange(tsCode, startDate, endDate));
        } catch (Exception e) {
            log.error("Error occurred while querying IndexWeight by ts_code and date range");
            log.error("Stack trace info", e);
        }

        return indexWeightList;
    }
}
