package com.kello.investment.service;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.dto.exception.InvalidPeriodException;
import com.kello.investment.dto.exception.SoldOutException;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.entity.InvestingStatus.Key;
import com.kello.investment.enums.RecruitingStatusEnum;
import com.kello.investment.enums.ResultCodeEnum;
import com.kello.investment.repository.InvestingProductRepository;
import com.kello.investment.repository.InvestingStatusRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InvestingService {

  private final InvestingProductRepository productRepository;
  private final InvestingStatusRepository statusRepository;

  @Value("${spring.application.timeZone:Asia/Seoul}")
  private String timeZone;

  public InvestingService(
      InvestingProductRepository productRepository,
      InvestingStatusRepository statusRepository) {
    this.productRepository = productRepository;
    this.statusRepository = statusRepository;
  }

  public CommonResponse<List<InvestingProductDto>> getAllInvestmentProducts() {
    var allProduct = productRepository.findAllValidProduct(getLocalDateTime())
        .stream()
        .peek(p -> {
          if (p.getPresentInvestingAmount() < p.getTotalInvestingAmount()) {
            p.setRecruitingStatus(RecruitingStatusEnum.RECRUITING);
          } else {
            p.setRecruitingStatus(RecruitingStatusEnum.COMPLETED);
          }
        })
        .collect(Collectors.toList());

    return new CommonResponse<List<InvestingProductDto>>()
        .of(ResultCodeEnum.NORMAL, timeZone)
        .result(allProduct)
        .build();
  }

  @Transactional
  public CommonResponse<InvestingStatus> invest(long userId, long productId, long amount) {
    checkValidRequest(productId, amount);

    var investingStatus = statusRepository.findById(new Key(productId, userId));
    if (investingStatus.isPresent()) {
      amount += investingStatus.get().getInvestAmount();
    }

    return new CommonResponse<InvestingStatus>()
        .of(ResultCodeEnum.NORMAL, timeZone)
        .result(statusRepository.save(InvestingStatus.builder()
            .productId(productId)
            .userId(userId)
            .investAmount(amount)
            .investDate(getLocalDateTime())
            .build()))
        .build();
  }

  private void checkValidRequest(long productId, long amount) {
    if (!isValidPeriod(productId)) {
      throw new InvalidPeriodException();
    }

    if (isExceedAmount(productId, amount)) {
      throw new SoldOutException();
    }
  }

  private boolean isValidPeriod(long productId) {
    return !productRepository.getComparedTimes(productId, getLocalDateTime())
        .containsValue(false);
  }

  private boolean isExceedAmount(long productId, long amount) {
    return productRepository.isExceedAmount(productId, amount);
  }

  private LocalDateTime getLocalDateTime() {
    return LocalDateTime.now(ZoneId.of(timeZone));
  }

  public CommonResponse<List<MyInvestingProductDto>> getMyInvestmentProduct(long userId) {
    return new CommonResponse<List<MyInvestingProductDto>>()
        .of(ResultCodeEnum.NORMAL, timeZone)
        .result(statusRepository.findAllByUserId(userId))
        .build();
  }
}
