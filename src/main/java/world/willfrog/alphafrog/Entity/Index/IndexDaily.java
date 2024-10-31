package world.willfrog.alphafrog.Entity.Index;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import world.willfrog.alphafrog.Entity.Common.Quote;


@Entity
@Table(name = "alphafrog_index_daily",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ts_code", "trade_date"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class IndexDaily extends Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long indexDailyId;

}
