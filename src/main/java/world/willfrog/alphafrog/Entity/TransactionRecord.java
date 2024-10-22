package world.willfrog.alphafrog.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "alphafrog_transaction_record")
public class TransactionRecord {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_record_id")
  private Long transactionRecordId;

  @Column(name = "transaction_record_time")
  private Long transactionRecordTime;

  @Column(name = "transaction_record_ts_code")
  private String transactionRecordTsCode;
  
  @Column(name = "transaction_record_amount")
  private Double transactionRecordAmount;

  @Column(name = "transaction_record_nav")
  private Double transactionRecordNav;

  @Column(name = "transaction_record_shares")
  private Double transactionRecordShares;

  @Column(name = "transaction_record_fee")
  private Double transactionRecordFee;

  @Column(name = "transaction_record_type")
  private TransactionRecordType transactionRecordType;

  public enum TransactionRecordType {
    BUY,
    SELL,
    DIVIDEND,
    TRANSFER_IN,
    TRANSFER_OUT
  }



}
