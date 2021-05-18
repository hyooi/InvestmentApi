package com.kello.investment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.tuple;

import com.kello.investment.entity.InvestingProduct;
import com.kello.investment.entity.InvestingStatus;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EntityScan(basePackages = "com.kello.investment.entity")
class InvestingProductRepositoryTest {

  @Autowired
  private InvestingProductRepository productRepository;

  @Autowired
  private InvestingStatusRepository statusRepository;

  @BeforeEach
  void init() {
    var product1 = InvestingProduct.builder()
        .productId(1)
        .title("title1")
        .totalInvestingAmount(10000)
        .startedAt(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0))
        .finishedAt(LocalDateTime.of(2021, Month.MARCH, 30, 0, 0))
        .build();
    var product2 = InvestingProduct.builder()
        .productId(2)
        .title("title2")
        .totalInvestingAmount(20000)
        .startedAt(LocalDateTime.of(2021, Month.FEBRUARY, 1, 0, 0))
        .finishedAt(LocalDateTime.of(2021, Month.APRIL, 30, 0, 0))
        .build();
    var product3 = InvestingProduct.builder()
        .productId(3)
        .title("title3")
        .totalInvestingAmount(30000)
        .startedAt(LocalDateTime.of(2021, Month.FEBRUARY, 1, 0, 0))
        .finishedAt(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0))
        .build();

    productRepository.deleteAll();
    productRepository.saveAll(Arrays.asList(product1, product2, product3));
  }

  @Test
  @DisplayName("다건 저장")
  void test_saveAll() {
    var product1 = InvestingProduct.builder()
        .productId(1)
        .title("title01-1")
        .build();
    var product2 = InvestingProduct.builder()
        .productId(5)
        .title("title05")
        .build();

    productRepository.saveAll(Arrays.asList(product1, product2));

    assertThat(productRepository.findAll()).hasSize(4);
    assertThat(productRepository.findById(1L).get().getTitle()).isEqualTo("title01-1");
    assertThat(productRepository.findById(5L).get().getTitle()).isEqualTo("title05");
  }

  @Test
  @DisplayName("현재 시간에서 유효한 투자상품 조회-투자자 없음")
  void test_findAllValidProduct_noneStatus() {
    var result = productRepository
        .findAllValidProduct(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0));

    assertThat(result)
        .hasSize(2)
        .extracting("title", "totalInvestingAmount", "presentInvestingAmount",
            "investorCnt")
        .containsExactly(tuple("title1", 10000L, 0L, 0L),
            tuple("title2", 20000L, 0L, 0L));
  }

  @Test
  @DisplayName("현재 시간에서 유효한 투자상품 조회-투자자 있음")
  void test_findAllValidProduct_withStatus() {
    statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(5000)
        .build());

    var result = productRepository
        .findAllValidProduct(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0));

    assertThat(result)
        .hasSize(2)
        .extracting("title", "totalInvestingAmount", "presentInvestingAmount",
            "investorCnt")
        .containsExactly(tuple("title1", 10000L, 5000L, 1L),
            tuple("title2", 20000L, 0L, 0L));
  }

  @Test
  @DisplayName("현재 시간에서 유효여부 조회")
  void test_getComparedTimes() {
    var result1 = productRepository.getComparedTimes(1, LocalDateTime.of(2021, Month.MARCH, 1, 0,
        0));
    var result2 = productRepository.getComparedTimes(1, LocalDateTime.of(2021, Month.MARCH, 30, 0,
        0));

    assertThat(result1)
        .contains(entry("validStartTime", true), entry("validEndTime", true));
    assertThat(result2)
        .contains(entry("validStartTime", true), entry("validEndTime", false));
  }

  @Test
  @DisplayName("요청금액 투자금 초과여부 조회-투자금액 없음")
  void test_isPossibleInvestment_noneStatus() {
    var temp1 = productRepository.findById(1L);
    var temp2 = statusRepository.findAll();

    assertThat(productRepository.isExceedAmount(1, 10000)).isFalse();
  }

  @Test
  @DisplayName("요청금액 투자금 초과여부 조회-총 투자금액이 투자가능 금액 초과")
  void test_isPossibleInvestment_exceedTotalAmount() {
    statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(10000)
        .build());

    assertThat(productRepository.isExceedAmount(1, 10000)).isTrue();
  }

  @Test
  @DisplayName("요청금액 투자금 초과여부 조회-총 투자금액은 투자가능 금액 미만이나, 추가 투자 시 초과")
  void test_isPossibleInvestment_exceedWhenAddInvestment() {
    statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(8000)
        .build());

    assertThat(productRepository.isExceedAmount(1, 10000)).isTrue();
  }
}