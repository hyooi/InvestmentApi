package com.kello.investment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCodeEnum {
  NORMAL("000000", "NORMAL"),
  SOLD_OUT("E00001", "SOLD OUT"),
  E99999("E99999", "Unknown error");

  private final String resultCode;
  private final String resultMessage;
}
