package world.willfrog.alphafrog.Entity.Index;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import world.willfrog.alphafrog.Entity.Common.Quote;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table( name = "alphafrog_index_weekly",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"ts_code", "trade_date"})
        } )
public class IndexWeekly extends Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long indexWeeklyId;
}
