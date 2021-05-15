package com.kello.investment.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class InvestingProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long productId;

  private String title;
  private Long totalInvestingAmount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
}
