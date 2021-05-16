package com.kello.investment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kello.investment.entity.InvestingStatus;
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
  private InvestingStatusRepository repository;

  @Test
  @DisplayName("findById")
  void test_findById() {

  }

  @Test
  @DisplayName("save")
  void test_save() {
    repository.save(InvestingStatus.builder()
        .productId(1L)
        .userId("user01")
        .investAmount(10000L)
        .build());

    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("findAllByUserId")
  void test_findAllByUserId() {

  }
}