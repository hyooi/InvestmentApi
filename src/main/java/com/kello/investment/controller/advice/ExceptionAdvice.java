package com.kello.investment.controller.advice;

import com.kello.investment.dto.ErrorResponse;
import com.kello.investment.dto.exception.InvalidPeriodException;
import com.kello.investment.dto.exception.SoldOutException;
import com.kello.investment.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

  @Value("${spring.application.timeZone:Asia/Seoul}")
  private String timeZone;

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = InvalidPeriodException.class)
  public ErrorResponse exceptionHandler(InvalidPeriodException e) {
    log.error("Exception occurred.", e);

    return ErrorResponse.of(ErrorCodeEnum.INVALID_PERIOD, timeZone);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = SoldOutException.class)
  public ErrorResponse exceptionHandler(SoldOutException e) {
    log.error("Exception occurred.", e);

    return ErrorResponse.of(ErrorCodeEnum.SOLD_OUT, timeZone);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = Throwable.class)
  public ErrorResponse exceptionHandler(Throwable e) {
    log.error("Exception occurred.", e);

    return ErrorResponse.of(ErrorCodeEnum.UNKNOWN_ERROR, timeZone);
  }
}
