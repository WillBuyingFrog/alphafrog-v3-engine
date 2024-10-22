package world.willfrog.alphafrog.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import world.willfrog.alphafrog.Entity.TransactionRecord;

@Mapper
public interface TransactionRecordDao {
  int insertTransactionRecord(TransactionRecord transactionRecord);

  @Select("SELECT * FROM alphafrog_transaction_record ORDER BY transaction_record_time DESC LIMIT #{pageSize} OFFSET (#{page} - 1) * #{pageSize}")
  List<TransactionRecord> getTransactionRecords(int page, int pageSize);

  @Select("SELECT COUNT(*) FROM alphafrog_transaction_record")
  int getTransactionRecordsCount();

}
