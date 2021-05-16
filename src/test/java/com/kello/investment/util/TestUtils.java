package com.kello.investment.util;

public class TestUtils {

  public static long randomLongId() {
    return 100000 + (long) (Math.random() * (999999 - 100000));
  }
}
