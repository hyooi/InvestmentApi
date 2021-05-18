package com.kello.investment.controller.api;

import static com.kello.investment.util.TestUtils.randomLongId;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.enums.ResultCodeEnum;
import com.kello.investment.service.InvestingService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvestingController.class)
class InvestingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InvestingService service;

  @Test
  @DisplayName("전체 투자상품 조회API 테스트")
  @SneakyThrows
  void test_getAllInvestmentProducts() {
    var productId1 = randomLongId();
    var productId2 = randomLongId();

    var resultList = new CommonResponse<List<InvestingProductDto>>()
        .of(ResultCodeEnum.NORMAL, "Asia/Seoul")
        .result(Arrays.asList(getInvestingProductDto(productId1),
            getInvestingProductDto(productId2)))
        .build();

    given(service.getAllInvestmentProducts()).willReturn(resultList);

    mockMvc.perform(get("/api/products"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result[0].productId", Matchers.is((int) productId1)))
        .andExpect(jsonPath("result[0].title", Matchers.is("title")))
        .andExpect(jsonPath("result[1].productId", Matchers.is((int) productId2)))
        .andExpect(jsonPath("result[1].title", Matchers.is("title")))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));

    then(service).should(times(1))
        .getAllInvestmentProducts();
  }

  private InvestingProductDto getInvestingProductDto(long productId) {
    return new InvestingProductDto(productId, "title", 10L, LocalDateTime.now(),
        LocalDateTime.now(), 0L, 1L);
  }

  @Test
  @DisplayName("투자하기API 테스트")
  @SneakyThrows
  void test_invest() {
    var productId = randomLongId();
    var userId = randomLongId();
    var amount = 10000;

    var resultList = new CommonResponse<InvestingStatus>()
        .of(ResultCodeEnum.NORMAL, "Asia/Seoul")
        .result(InvestingStatus.builder()
            .productId(productId)
            .userId(userId)
            .investAmount(amount)
            .build())
        .build();

    given(service.invest(userId, productId, amount)).willReturn(resultList);

    mockMvc.perform(post("/api/invest/" + productId + "/" + amount)
        .header("X-USER-ID", userId))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result.productId", Matchers.is((int) productId)))
        .andExpect(jsonPath("result.userId", Matchers.is((int) userId)))
        .andExpect(jsonPath("result.investAmount", Matchers.is(amount)))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));

    then(service).should(times(1))
        .invest(userId, productId, amount);
  }

  @Test
  @DisplayName("나의투자상품조회API 테스트")
  @SneakyThrows
  void test_getMyInvestmentProduct() {
    var productId1 = randomLongId();
    var productId2 = randomLongId();
    var userId = randomLongId();

    var resultList = new CommonResponse<List<MyInvestingProductDto>>()
        .of(ResultCodeEnum.NORMAL, "Asia/Seoul")
        .result(Arrays.asList(getMyInvestingProductDto(productId1),
            getMyInvestingProductDto(productId2)))
        .build();

    given(service.getMyInvestmentProduct(userId)).willReturn(resultList);

    mockMvc.perform(get("/api/product")
        .header("X-USER-ID", userId))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("resultCode", Matchers.is(ResultCodeEnum.NORMAL.getResultCode())))
        .andExpect(jsonPath("result[0].productId", Matchers.is((int) productId1)))
        .andExpect(jsonPath("result[0].title", Matchers.is("title")))
        .andExpect(jsonPath("result[1].productId", Matchers.is((int) productId2)))
        .andExpect(jsonPath("result[1].title", Matchers.is("title")))
        .andExpect(jsonPath("timestamp", Matchers.notNullValue()));

    then(service).should(times(1))
        .getMyInvestmentProduct(userId);
  }

  private MyInvestingProductDto getMyInvestingProductDto(long productId) {
    return new MyInvestingProductDto(productId, "title", 10000L, 100L, LocalDateTime.now());
  }

}