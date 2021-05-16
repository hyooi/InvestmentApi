package com.kello.investment.dto;

import com.kello.investment.enums.ResultCodeEnum;
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
public class CommonResponse<T> {

  private String resultCode;
  private T result;
  private LocalDateTime timestamp;

  public CommonResponseBuilder<T> of(ResultCodeEnum resultCodeEnum, String timeZone) {
    return CommonResponse.<T>builder()
        .resultCode(resultCodeEnum.getResultCode())
        .timestamp(LocalDateTime.now(ZoneId.of(timeZone)));
  }
}
