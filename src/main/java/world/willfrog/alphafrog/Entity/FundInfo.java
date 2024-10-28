package world.willfrog.alphafrog.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alphafrog_fund_info",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ts_code"})
        })
public class FundInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long fundPortfolioId;

  // 基金代码
  @Column(name = "ts_code", nullable = false)
  String tsCode;

  // 简称
  @Column(name = "name")
  String name;

  // 管理人
  @Column(name = "management")
  String management;

  // 托管人
  @Column(name = "custodian")
  String custodian;

  // 投资类型
  @Column(name = "fund_type")
  String fundType;

  // 成立日期
  @Column(name = "found_date")
  Long foundDate;

  // 到期日期
  @Column(name = "due_date")
  Long dueDate;

  // 上市时间
  @Column(name = "list_date")
  Long listDate;

  // 发行日期
  @Column(name = "issue_date")
  Long issueDate;

  // 退市日期
  @Column(name = "delist_date")
  Long delistDate;

  // 发行份额（亿份）
  @Column(name = "issue_amount")
  Float issueAmount;

  // 管理费
  @Column(name = "m_fee")
  Float mFee;

  // 托管费
  @Column(name = "c_fee")
  Float cFee;

  // 存续期
  @Column(name = "duration_year")
  Float durationYear;

  // 面值
  @Column(name = "p_value")
  Float pValue;

  // 起点金额（万元）
  @Column(name = "min_amount")
  Float minAmount;

  // 预期收益率
  @Column(name = "exp_return")
  Float expReturn;

  // 业绩比较基准
  @Column(name = "benchmark", length = 500)
  String benchmark;

  // 存续状态或摘牌
  @Column(name = "status", length = 2)
  String status;

  // 投资风格
  @Column(name = "invest_type")
  String investType;

  // 基金类型
  @Column(name = "type", length = 10)
  String type;

  // 受托人
  @Column(name = "trustee", length = 20)
  String trustee;

  // 日常申购起始日
  @Column(name = "purc_startdate")
  Long purcStartDate;

  // 日常赎回起始日
  @Column(name = "redm_startdate")
  Long redmStartDate;

  // E场内O场外
  @Column(name = "market", length = 2)
  String market;
}