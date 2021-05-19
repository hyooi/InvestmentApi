package com.kello.investment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.dto.exception.InvalidPeriodException;
import com.kello.investment.dto.exception.SoldOutException;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.enums.RecruitingStatusEnum;
import com.kello.investment.enums.ResultCodeEnum;
import com.kello.investment.repository.InvestingProductRepository;
import com.kello.investment.repository.InvestingStatusRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InvestingService.class)
class InvestingServiceTest {

  @Autowired
  private InvestingService service;

  @MockBean
  private InvestingProductRepository productRepository;

  @MockBean
  private InvestingStatusRepository statusRepository;

  @Test
  @DisplayName("유효한 전체 투자상품 조회 테스트")
  void test_getAllInvestmentProducts() {
    given(productRepository.findAllValidProduct(any(LocalDateTime.class)))
        .willReturn(Arrays.asList(getInvestingProductDto(1, 10000, 100, 1)
            , getInvestingProductDto(2, 20000, 1000, 2)
            , getInvestingProductDto(3, 20000, 20000, 3)));

    var result = service.getAllInvestmentProducts();

    then(productRepository)
        .should(times(1))
        .findAllValidProduct(any(LocalDateTime.class));

    assertThat(result)
        .hasFieldOrPropertyWithValue("resultCode", ResultCodeEnum.NORMAL.getResultCode())
        .hasNoNullFieldsOrProperties();

    assertThat(result.getResult())
        .hasSize(3)
        .extracting("productId", "title", "totalInvestingAmount", "presentInvestingAmount",
            "investorCnt", "recruitingStatus")
        .contains(tuple(1L, "title", 10000L, 100L, 1L, RecruitingStatusEnum.RECRUITING),
            tuple(2L, "title", 20000L, 1000L, 2L, RecruitingStatusEnum.RECRUITING),
            tuple(3L, "title", 20000L, 20000L, 3L, RecruitingStatusEnum.COMPLETED));
  }

  private InvestingProductDto getInvestingProductDto(int productId, int totalInvestingAmount,
      int presentInvestingAmount, int investorCnt) {
    return new InvestingProductDto(productId, "title", totalInvestingAmount, LocalDateTime.now(),
        LocalDateTime.now(), presentInvestingAmount, investorCnt);
  }

  @Test
  @DisplayName("투자 테스트-투자 불가 기간인 상품 요청")
  void test_invest1() {
    given(productRepository.getComparedTimes(any(Long.class), any(LocalDateTime.class)))
        .willReturn(Map.of("validStartTime", true, "validEndTime", false));

    assertThatThrownBy(() -> service.invest(10, 10, 10000))
        .isInstanceOf(InvalidPeriodException.class);

    then(productRepository)
        .should(times(1))
        .getComparedTimes(any(Long.class), any(LocalDateTime.class));

    then(statusRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("투자 테스트-sold out상품 요청")
  void test_invest2() {
    given(productRepository.getComparedTimes(any(Long.class), any(LocalDateTime.class)))
        .willReturn(Map.of("validStartTime", true, "validEndTime", true));

    given(productRepository.isExceedAmount(any(Long.class), any(Long.class)))
        .willReturn(true);

    assertThatThrownBy(() -> service.invest(10, 10, 10000))
        .isInstanceOf(SoldOutException.class);

    then(productRepository)
        .should(times(1))
        .getComparedTimes(any(Long.class), any(LocalDateTime.class));

    then(productRepository)
        .should(times(1))
        .isExceedAmount(10, 10000);

    then(statusRepository).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("투자 테스트-신규 투자")
  void test_invest3() {
    given(productRepository.getComparedTimes(any(Long.class), any(LocalDateTime.class)))
        .willReturn(Map.of("validStartTime", true, "validEndTime", true));

    given(productRepository.isExceedAmount(any(Long.class), any(Long.class)))
        .willReturn(false);

    given(statusRepository.save(any(InvestingStatus.class)))
        .willReturn(InvestingStatus.builder()
            .productId(10)
            .userId(10)
            .investAmount(10000)
            .build());

    var result = service.invest(10, 10, 10000);

    then(statusRepository)
        .should(times(1))
        .findById(any(InvestingStatus.Key.class));

    then(statusRepository)
        .should(times(1))
        .save(any(InvestingStatus.class));

    assertThat(result)
        .hasFieldOrPropertyWithValue("resultCode", ResultCodeEnum.NORMAL.getResultCode())
        .hasNoNullFieldsOrProperties();

    assertThat(result.getResult())
        .hasFieldOrPropertyWithValue("productId", 10L)
        .hasFieldOrPropertyWithValue("userId", 10L)
        .hasFieldOrPropertyWithValue("investAmount", 10000L);
  }

  @Test
  @DisplayName("투자 테스트-재투자")
  void test_invest4() {
    given(productRepository.getComparedTimes(any(Long.class), any(LocalDateTime.class)))
        .willReturn(Map.of("validStartTime", true, "validEndTime", true));

    given(productRepository.isExceedAmount(any(Long.class), any(Long.class)))
        .willReturn(false);

    given(statusRepository.findById(any(InvestingStatus.Key.class)))
        .willReturn(Optional.of(InvestingStatus.builder()
            .investAmount(10000)
            .build()));
    given(statusRepository.save(any(InvestingStatus.class)))
        .willReturn(InvestingStatus.builder()
            .productId(10)
            .userId(10)
            .investAmount(20000)
            .build());

    var result = service.invest(10, 10, 10000);

    then(statusRepository)
        .should(times(1))
        .findById(any(InvestingStatus.Key.class));

    then(statusRepository)
        .should(times(1))
        .save(any(InvestingStatus.class));

    assertThat(result)
        .hasFieldOrPropertyWithValue("resultCode", ResultCodeEnum.NORMAL.getResultCode())
        .hasNoNullFieldsOrProperties();

    assertThat(result.getResult())
        .hasFieldOrPropertyWithValue("productId", 10L)
        .hasFieldOrPropertyWithValue("userId", 10L)
        .hasFieldOrPropertyWithValue("investAmount", 20000L);
  }

  @Test
  @DisplayName("내 투자상품 조회 테스트")
  void test_getMyInvestmentProduct() {
    given(statusRepository.findAllByUserId(any(Long.class)))
        .willReturn(Arrays.asList(getMyInvestingProductDto(1, 1000, 100)
            , getMyInvestingProductDto(2, 10000, 1000)
            , getMyInvestingProductDto(3, 10000, 10000)));

    var result = service.getMyInvestmentProduct(10);

    then(productRepository).shouldHaveNoInteractions();
    then(statusRepository)
        .should(times(1))
        .findAllByUserId(10);

    assertThat(result)
        .hasFieldOrPropertyWithValue("resultCode", ResultCodeEnum.NORMAL.getResultCode())
        .hasNoNullFieldsOrProperties();

    assertThat(result.getResult())
        .hasSize(3)
        .extracting("productId", "title", "totalInvestingAmount", "myInvestAmount")
        .contains(tuple(1L, "title", 1000L, 100L),
            tuple(2L, "title", 10000L, 1000L),
            tuple(3L, "title", 10000L, 10000L));
  }

  private MyInvestingProductDto getMyInvestingProductDto(int productId, int totalInvestingAmount,
      int myInvestAmount) {
    return new MyInvestingProductDto(productId, "title", totalInvestingAmount, myInvestAmount,
        LocalDateTime.now());
  }
}