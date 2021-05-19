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

  private final long productId;
  private final String title;
  private final long totalInvestingAmount;
  private final LocalDateTime startedAt;
  private final LocalDateTime finishedAt;

  private final long presentInvestingAmount;
  private final long investorCnt;
  private RecruitingStatusEnum recruitingStatus;
}
