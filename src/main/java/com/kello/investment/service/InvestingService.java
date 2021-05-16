package com.kello.investment.service;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.entity.InvestingStatus.Key;
import com.kello.investment.enums.ErrorCodeEnum;
import com.kello.investment.enums.RecruitingStatusEnum;
import com.kello.investment.repository.InvestingProductRepository;
import com.kello.investment.repository.InvestingStatusRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.val;
import org.springframework.stereotype.Service;

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

  public List<InvestingProductDto> getAllInvestmentProducts() {
    return productRepository.findAllProduct(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .stream()
        .peek(p -> {
          if (p.getPresentInvestingAmount() < p.getTotalInvestingAmount()) {
            p.setRecruitingStatus(RecruitingStatusEnum.RECRUITING);
          } else {
            p.setRecruitingStatus(RecruitingStatusEnum.COMPLETED);
          }
        })
        .collect(Collectors.toList());
  }

  @Transactional
  public CommonResponse invest(String userId, long productId, long amount) {
    if (isExceedAmount(productId, amount)) {
      return CommonResponse.builder()
          .hasError(true)
          .code(ErrorCodeEnum.SOLD_OUT.getErrCd())
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
        .investDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .build());

    return CommonResponse.builder()
        .hasError(false)
        .build();
  }

  private boolean isExceedAmount(long productId, long amount) {
    return productRepository.isExceedAmount(productId, amount);
  }


  //TODO 총 모집금액이 내가 투자한 상품이 현재 얼마나 모였는지를 확인하는건지? 아님 원래 총 모집금액인지?
  public List<MyInvestingProductDto> getMyInvestmentProduct(String userId) {
    return statusRepository.findAllByUserId(userId);
  }
}
