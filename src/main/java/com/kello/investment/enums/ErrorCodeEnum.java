package com.kello.investment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeEnum {
  SOLD_OUT("E00001", "SOLD-OUT"),
  E99999("E99999", "Unknown");

  private final String errCd;
  private final String errDesc;
}
