package com.kello.investment.dto;

import com.kello.investment.enums.RecruitingStatusEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class InvestingProductDto {

  private final Long productId;
  private final String title;
  private final Long totalInvestingAmount;
  private final LocalDateTime startedAt;
  private final LocalDateTime finishedAt;

  private final Long presentInvestingAmount;
  private final Long userCnt;
  private RecruitingStatusEnum recruitingStatus;
}
