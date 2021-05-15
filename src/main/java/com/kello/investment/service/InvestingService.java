package com.kello.investment.service;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.enums.ErrorCodeEnum;
import com.kello.investment.repository.InvestingProductRepository;
import com.kello.investment.repository.InvestingStatusRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class InvestingService {

  private final InvestingProductRepository productRepository;
  private final InvestingStatusRepository statusRepository;

  public InvestingService(
      InvestingProductRepository productRepository,
      InvestingStatusRepository statusRepository) {
    this.productRepository = productRepository;
    this.statusRepository = statusRepository;
  }

  @Transactional
  public CommonResponse invest(String userId, long productId, long amount) {
    if (isExceedAmount(productId, amount)) {
      return CommonResponse.builder()
          .hasError(true)
          .errCd(ErrorCodeEnum.SOLD_OUT.getErrCd())
          .build();
    }

    statusRepository.save(InvestingStatus.builder()
        .productId(productId)
        .userId(userId)
        .investAmount(amount)
        .investDate(LocalDateTime.now(ZoneId.of("KST")))
        .build());

    return CommonResponse.builder()
        .hasError(false)
        .build();
  }

  private boolean isExceedAmount(long productId, long amount) {
    return false;
  }
}
