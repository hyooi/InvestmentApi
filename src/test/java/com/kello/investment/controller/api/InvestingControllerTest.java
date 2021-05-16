package com.kello.investment.controller.api;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.service.InvestingService;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.val;
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
    given(service.getAllInvestmentProducts())
        .willReturn(CommonResponse.builder()
            .result(Arrays.asList(getInvestingProductDto(1),
                getInvestingProductDto(2)))
            .build());

    val actions = mockMvc.perform(get("/api/products"))
        .andDo(print());

    then(service).should(times(1))
        .getAllInvestmentProducts();

    actions.andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].productId", Matchers.is(1)))
        .andExpect(jsonPath("$[1].productId", Matchers.is(2)));

  }

  private InvestingProductDto getInvestingProductDto(long productId) {
    return new InvestingProductDto(productId, "title", 10L, LocalDateTime.now(),
        LocalDateTime.now(), 0L, 1L);
  }

  @Test
  @DisplayName("투자하기API 테스트")
  void test_getinvest() {
  }

  @Test
  @DisplayName("나의투자상품조회API 테스트")
  void test_getMyInvestmentProduct() {
  }


}