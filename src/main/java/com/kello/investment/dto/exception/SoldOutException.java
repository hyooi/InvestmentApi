package com.kello.investment.dto.exception;

public class SoldOutException extends RuntimeException {

  public SoldOutException() {
    super();
  }

  public SoldOutException(String s) {
    super(s);
  }

  public SoldOutException(String s, Throwable th) {
    super(s, th);
  }

  public SoldOutException(Throwable th) {
    super(th);
  }

}
