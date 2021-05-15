package com.kello.investment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestingProductDto {

  private Long productId;
  private String title;
  private Long totalInvestingAmount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  private Long presentInvestingAmount;
  private Long investingUserCnt;
  private String recruitingStatus;
  private String recruitingPeriod;
}
