package com.kello.investment.controller.advice;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

  @ExceptionHandler(value = Throwable.class)
  public CommonResponse exceptionHandler(Throwable e) {
    log.error("Exception occurred.", e);

    return CommonResponse.builder()
        .resultCode(ResultCodeEnum.E99999.getResultCode())
        .resultMessage(e.toString())
        .build();
  }
}
