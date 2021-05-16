package com.kello.investment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCodeEnum {
  SOLD_OUT("E00001", "SOLD OUT"),
  INVALID_PERIOD("E00002", "Not an investment period"),
  UNKNOWN_ERROR("E99999", "Unknown error");

  private final String errorCode;
  private final String errorMessage;
}
