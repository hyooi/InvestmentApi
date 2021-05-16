package com.kello.investment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MyInvestingProductDto {

  private final Long productId;
  private final String title;
  private final Long totalInvestingAmount;
  private final Long investAmount;
  private final LocalDateTime investDate;
}
