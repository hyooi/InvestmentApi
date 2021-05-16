package com.kello.investment.dto.exception;

public class InvalidPeriodException extends RuntimeException {

  public InvalidPeriodException() {
    super();
  }

  public InvalidPeriodException(String s) {
    super(s);
  }

  public InvalidPeriodException(String s, Throwable th) {
    super(s, th);
  }

  public InvalidPeriodException(Throwable th) {
    super(th);
  }

}
