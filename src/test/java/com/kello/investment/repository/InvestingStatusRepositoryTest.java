package com.kello.investment.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.kello.investment.entity.InvestingProduct;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.entity.InvestingStatus.Key;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
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
class InvestingStatusRepositoryTest {

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

    productRepository.deleteAll();
    statusRepository.deleteAll();
    productRepository.saveAll(Collections.singletonList(product1));
  }

  @Test
  @DisplayName("findById")
  void test_findById() {
    var entity = statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(1000)
        .build());

    assertThat(statusRepository.findById(new Key(1, 10)).get())
        .isEqualTo(entity);
  }

  @Test
  @DisplayName("save")
  void test_save() {
    statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(1000)
        .build());

    assertThat(statusRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("findAllByUserId-투자안한 경우")
  void test_findAllByUserId_none_invest() {
    assertThat(statusRepository.findAllByUserId(10)).hasSize(0);
  }

  @Test
  @DisplayName("findAllByUserId-투자한 경우")
  void test_findAllByUserId_invest() {
    statusRepository.save(InvestingStatus.builder()
        .productId(1)
        .userId(10)
        .investAmount(1000)
        .build());

    assertThat(statusRepository.findAllByUserId(10))
        .hasSize(1)
        .extracting("productId", "title", "totalInvestingAmount", "myInvestAmount")
        .contains(tuple(1L, "title1", 10000L, 1000L));
  }
}