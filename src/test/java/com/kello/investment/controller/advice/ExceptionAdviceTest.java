package com.kello.investment.controller.advice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kello.investment.dto.exception.InvalidPeriodException;
import com.kello.investment.dto.exception.SoldOutException;
import com.kello.investment.enums.ErrorCodeEnum;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {ExceptionAdvice.class,
    ExceptionAdviceTest.ExceptionTestController.class})
class ExceptionAdviceTest {

  private static final String INVALID_PERIOD_EXCEPTION_URI = "/1";
  private static final String SOLD_OUT_EXCEPTION_URI = "/2";
  private static final String THROWABLE_URI = "/3";

  @Autowired
  private MockMvc mockMvc;

  @RestController
  static class ExceptionTestController {

    @GetMapping(INVALID_PERIOD_EXCEPTION_URI)
    public void test1() {
      throw new InvalidPeriodException();
    }

    @GetMapping(SOLD_OUT_EXCEPTION_URI)
    public void test2() {
      throw new SoldOutException();
    }

    @GetMapping(THROWABLE_URI)
    public void test3() {
      throw new NullPointerException();
    }

  }

  @Test
  @SneakyThrows
  public void test_InvalidPeriodException() {
    mockMvc.perform(get(INVALID_PERIOD_EXCEPTION_URI))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andExpect(jsonPath("errorCode", Matchers.is(ErrorCodeEnum.INVALID_PERIOD.getErrorCode())))
        .andExpect(
            jsonPath("errorMessage", Matchers.is(ErrorCodeEnum.INVALID_PERIOD.getErrorMessage())))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }

  @Test
  @SneakyThrows
  public void test_SoldOutException() {
    mockMvc.perform(get(SOLD_OUT_EXCEPTION_URI))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andExpect(jsonPath("errorCode", Matchers.is(ErrorCodeEnum.SOLD_OUT.getErrorCode())))
        .andExpect(jsonPath("errorMessage", Matchers.is(ErrorCodeEnum.SOLD_OUT.getErrorMessage())))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }

  @Test
  @SneakyThrows
  public void test_Throwable() {
    mockMvc.perform(get(THROWABLE_URI))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andExpect(jsonPath("errorCode", Matchers.is(ErrorCodeEnum.UNKNOWN_ERROR.getErrorCode())))
        .andExpect(
            jsonPath("errorMessage", Matchers.is(ErrorCodeEnum.UNKNOWN_ERROR.getErrorMessage())))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));
  }

}