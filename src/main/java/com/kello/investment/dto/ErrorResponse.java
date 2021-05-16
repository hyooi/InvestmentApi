package com.kello.investment.dto;

import com.kello.investment.enums.ErrorCodeEnum;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private String errorCode;
  private String errorMessage;
  private LocalDateTime timestamp;

  public static ErrorResponse of(ErrorCodeEnum resultCodeEnum, String timeZone) {
    return ErrorResponse.builder()
        .errorCode(resultCodeEnum.getErrorCode())
        .errorMessage(resultCodeEnum.getErrorMessage())
        .timestamp(LocalDateTime.now(ZoneId.of(timeZone)))
        .build();
  }
}
