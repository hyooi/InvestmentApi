package com.kello.investment.service;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
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
import lombok.val;
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
    val allProduct = productRepository.findAllProduct(getLocalDateTime())
        .stream()
        .peek(p -> {
          if (p.getPresentInvestingAmount() < p.getTotalInvestingAmount()) {
            p.setRecruitingStatus(RecruitingStatusEnum.RECRUITING);
          } else {
            p.setRecruitingStatus(RecruitingStatusEnum.COMPLETED);
          }
        })
        .collect(Collectors.toList());

    return CommonResponse.<List<InvestingProductDto>>builder()
        .resultCode(ResultCodeEnum.NORMAL.getResultCode())
        .result(allProduct)
        .timestamp(getLocalDateTime())
        .build();
  }

  @Transactional
  @SuppressWarnings("rawtypes")
  public CommonResponse invest(String userId, long productId, long amount) {
    if (isExceedAmount(productId, amount)) {
      return CommonResponse.builder()
          .resultCode(ResultCodeEnum.SOLD_OUT.getResultCode())
          .resultMessage(ResultCodeEnum.SOLD_OUT.getResultMessage())
          .timestamp(getLocalDateTime())
          .build();
    }

    val investingStatus = statusRepository.findById(new Key(productId, userId));
    if (investingStatus.isPresent()) {
      amount += investingStatus.get().getInvestAmount();
    }

    statusRepository.save(InvestingStatus.builder()
        .productId(productId)
        .userId(userId)
        .investAmount(amount)
        .investDate(getLocalDateTime())
        .build());

    return CommonResponse.builder()
        .resultCode(ResultCodeEnum.NORMAL.getResultCode())
        .resultMessage(ResultCodeEnum.NORMAL.getResultMessage())
        .timestamp(getLocalDateTime())
        .build();
  }

  private boolean isExceedAmount(long productId, long amount) {
    return productRepository.isExceedAmount(productId, amount);
  }


  //TODO 총 모집금액이 내가 투자한 상품이 현재 얼마나 모였는지를 확인하는건지? 아님 원래 총 모집금액인지?
  public CommonResponse<List<MyInvestingProductDto>> getMyInvestmentProduct(String userId) {
    return CommonResponse.<List<MyInvestingProductDto>>builder()
        .resultCode(ResultCodeEnum.NORMAL.getResultCode())
        .result(statusRepository.findAllByUserId(userId))
        .timestamp(getLocalDateTime())
        .build();
  }


  private LocalDateTime getLocalDateTime() {
    return LocalDateTime.now(ZoneId.of(timeZone));
  }
}
