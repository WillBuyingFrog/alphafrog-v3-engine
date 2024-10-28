package world.willfrog.alphafrog.Service.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import org.springframework.stereotype.Service;
import world.willfrog.alphafrog.Dao.TransactionRecordDao;
import world.willfrog.alphafrog.Entity.TransactionRecord;
import world.willfrog.alphafrog.Service.TransactionRecordService;

@Service
public class TransactionRecordServiceImpl implements TransactionRecordService {

    @Autowired
    private TransactionRecordDao transactionRecordDao;

    @Override
    public void saveTransactionRecord(TransactionRecord transactionRecord) {
        transactionRecordDao.insertTransactionRecord(transactionRecord);
    }

    @Override
    public List<TransactionRecord> getTransactionRecords(int page, int pageSize) {
        return transactionRecordDao.getTransactionRecords(page, pageSize);
    }
}
