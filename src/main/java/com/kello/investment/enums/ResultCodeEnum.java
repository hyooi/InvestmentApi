package com.kello.investment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResultCodeEnum {
  NORMAL("N00000");

  private final String resultCode;
}
