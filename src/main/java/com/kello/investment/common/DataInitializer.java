package com.kello.investment.common;

import com.kello.investment.entity.InvestingProduct;
import com.kello.investment.repository.InvestingProductRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

  private final InvestingProductRepository repository;

  @Value("${spring.application.timeZone:Asia/Seoul}")
  private String timeZone;
  
  public DataInitializer(InvestingProductRepository repository) {
    this.repository = repository;
  }

  @Override
  public void run(ApplicationArguments args) {
    var initialProducts = Arrays.asList(
        InvestingProduct.builder()
            .productId(1)
            .title("개인신용 포트폴리오")
            .totalInvestingAmount(1000000L)
            .startedAt(LocalDateTime.of(2021, Month.MARCH, 1, 0, 0))
            .finishedAt(LocalDateTime.of(2021, Month.MARCH, 8, 0, 0))
            .build(),
        InvestingProduct.builder()
            .productId(2)
            .title("부동산 포트폴리오")
            .totalInvestingAmount(5000000L)
            .startedAt(LocalDateTime.now(ZoneId.of(timeZone)).minusDays(1))
            .finishedAt(LocalDateTime.now(ZoneId.of(timeZone)).plusDays(1))
            .build());

    repository.saveAll(initialProducts);
  }
}
