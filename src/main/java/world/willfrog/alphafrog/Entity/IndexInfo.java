package world.willfrog.alphafrog.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.util.ProxyUtils;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alphafrog_index_info",
              uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ts_code"})
              })
public class IndexInfo {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  Long indexInfoId;


  @Column(name = "ts_code", nullable = false)
  String tsCode;

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "fullname")
  String fullName;

  @Column(name = "market", nullable = false)
  String market;

  @Column(name = "publisher")
  String publisher;

  @Column(name = "index_type")
  String indexType;

  @Column(name = "category")
  String category;

  @Column(name = "base_date")
  Long baseDate;

  @Column(name = "base_point")
  Double basePoint;

  @Column(name = "list_date")
  Long listDate;

  @Column(name = "weight_rule")
  String weightRule;

  @Column(name = "desc")
  String desc;

  @Column(name = "exp_date")
  Long expDate;



}
