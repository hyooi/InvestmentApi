package com.kello.investment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.tuple;

import com.kello.investment.entity.InvestingProduct;
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

  @BeforeEach
  void init() {
    var product1 = InvestingProduct.builder()
        .productId(1)
        .title("title1")
        .totalInvestingAmount(10000)
        .presentInvestingAmount(2000)
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
  @DisplayName("?????? ??????")
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
  @DisplayName("?????? ???????????? ????????? ???????????? ??????")
  void test_findAllValidProduct() {
    var result = productRepository
        .findAllValidProduct(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0));

    assertThat(result)
        .hasSize(2)
        .extracting("title", "totalInvestingAmount", "presentInvestingAmount")
        .containsExactly(tuple("title1", 10000L, 2000L),
            tuple("title2", 20000L, 0L));
  }

  @Test
  @DisplayName("?????? ???????????? ???????????? ??????")
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
  @DisplayName("???????????? ????????? ???????????? ??????-??????")
  void test_isPossibleInvestment_noneStatus() {
    assertThat(productRepository.isExceedAmount(1, 8000)).isFalse();
  }

  @Test
  @DisplayName("???????????? ????????? ???????????? ??????-??? ??????????????? ???????????? ?????? ??????")
  void test_isPossibleInvestment_exceedTotalAmount() {
    productRepository.save(InvestingProduct.builder()
        .productId(1)
        .presentInvestingAmount(20000)
        .build());

    assertThat(productRepository.isExceedAmount(1, 10000)).isTrue();
  }

  @Test
  @DisplayName("???????????? ????????? ???????????? ??????-??? ??????????????? ???????????? ?????? ????????????, ?????? ?????? ??? ??????")
  void test_isPossibleInvestment_exceedWhenAddInvestment() {
    productRepository.save(InvestingProduct.builder()
        .productId(1)
        .presentInvestingAmount(1000)
        .build());

    assertThat(productRepository.isExceedAmount(1, 10000)).isTrue();
  }
}