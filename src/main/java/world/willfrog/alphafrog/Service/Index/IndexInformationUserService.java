package world.willfrog.alphafrog.Service.Index;

import world.willfrog.alphafrog.Entity.Index.IndexDaily;
import world.willfrog.alphafrog.Entity.Index.IndexInfo;
import world.willfrog.alphafrog.Entity.Index.IndexWeight;

import java.util.List;

public interface IndexInformationUserService {

    List<IndexInfo> getIndexInfoByTsCode(String tsCode);

    List<IndexInfo> getIndexInfoByNames(String queryName);

    List<IndexDaily> getIndexDailyByTsCodeAndDateRange(String tsCode, String startDate, String endDate);

    List<IndexWeight> getIndexWeightByTsCodeAndDateRange(String tsCode, String startDate, String endDate);
}
