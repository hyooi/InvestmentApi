package com.kello.investment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CommonResponse {

  private boolean hasError;
  private String errCd;
  private String errMsg;
}
