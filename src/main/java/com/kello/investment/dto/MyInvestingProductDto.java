package com.kello.investment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MyInvestingProductDto {

  private final long productId;
  private final String title;
  private final long totalInvestingAmount;
  private final long myInvestAmount;
  private final LocalDateTime investDate;
}
