package com.kello.investment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCodeEnum {
  NORMAL("S00000");

  private final String resultCode;
}
