package world.willfrog.alphafrog.Entity.Stock;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table( name = "alphafrog_stock_info",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ts_code", "symbol"})
        })
public class StockInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TuShare代码
    @Column(name = "ts_code", nullable = false)
    private String tsCode;

    // 股票代码
    @Column(name = "symbol", nullable = false)
    private String symbol;

    // 股票名称
    @Column(name = "name", nullable = false)
    private String name;

    // 地域
    @Column(name = "area")
    private String area;

    // 所属行业
    @Column(name = "industry")
    private String industry;

    // 股票全称
    @Column(name = "fullname")
    private String fullName;

    // 英文全称
    @Column(name = "enname")
    private String enName;

    // 拼音缩写
    @Column(name = "cnspell")
    private String cnSpell;

    // 市场类型
    @Column(name = "market")
    private String market;

    // 交易所代码
    @Column(name = "exchange")
    private String exchange;

    // 交易货币
    @Column(name = "curr_type")
    private String currType;

    // 上市状态
    @Column(name = "list_status")
    private String listStatus;

    // 上市日期
    @Column(name = "list_date")
    private Long listDate;

    // 退市日期
    @Column(name = "delist_date")
    private Long deListDate;

    // 是否是沪深港通标的，N否，H沪股通，S深股通
    @Column(name = "is_hs")
    private String isHs;

    // 实控人名称
    @Column(name = "act_name")
    private String actName;

    // 实控人企业性质
    @Column(name = "act_ent_type")
    private String actEntType;

}
