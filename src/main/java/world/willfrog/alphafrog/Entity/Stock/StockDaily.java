package world.willfrog.alphafrog.Entity.Stock;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import world.willfrog.alphafrog.Entity.Common.Quote;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alphafrog_stock_daily",
       uniqueConstraints = {
              @UniqueConstraint(columnNames = {"ts_code", "trade_date"})
       })
public class StockDaily extends Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
