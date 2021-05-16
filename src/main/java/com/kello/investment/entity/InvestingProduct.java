package com.kello.investment.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestingProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long productId;

  private String title;
  private long totalInvestingAmount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  @OneToMany(mappedBy = "investProduct")
  private List<InvestingStatus> status;
}
