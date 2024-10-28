package world.willfrog.alphafrog.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alphafrog_fund_nav",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ts_code", "nav_date"})
        })
public class FundNav {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long fundNavId;

    @Column(name = "ts_code", nullable = false)
    String tsCode;

    @Column(name = "ann_date")
    Long annDate;

    @Column(name = "nav_date", nullable = false)
    Long navDate;

    @Column(name = "unit_nav")
    Double unitNav;

    @Column(name = "accum_nav")
    Double accumNav;

    @Column(name = "net_asset")
    Double netAsset;

    @Column(name = "total_net_asset")
    Double totalNetAsset;

    @Column(name = "adj_nav")
    Double adjNav;

}
