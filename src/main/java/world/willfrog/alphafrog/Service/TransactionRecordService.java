package world.willfrog.alphafrog.Service;

import java.util.List;

import world.willfrog.alphafrog.Entity.TransactionRecord;

public interface TransactionRecordService {

  void saveTransactionRecord(TransactionRecord transactionRecord);

  List<TransactionRecord> getTransactionRecords(int page, int pageSize);
}
