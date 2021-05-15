package com.kello.investment.controller.api;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.service.InvestingService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@Slf4j
public class InvestingController {

  private static final String USER_ID_HEADER = "X-USER-ID";
  private final InvestingService service;

  public InvestingController(InvestingService service) {
    this.service = service;
  }

  @GetMapping("/products")
  public List<InvestingProductDto> getAllInvestmentProducts() {
    log.info("getAllInvestmentProducts");
    // 상품 모집기간 내의 상품만 응답

    return null;
  }

  @PostMapping("/invest")
  public CommonResponse invest(@RequestHeader(USER_ID_HEADER) String userId, long productId,
      long amount) {
    log.info("invest request: [userId: {}, productId: {}, amount: {}]", userId, productId, amount);
    return service.invest(userId, productId, amount);
  }

  @GetMapping("/myproduct")
  public List<MyInvestingProductDto> getMyInvestmentProduct(
      @RequestHeader(USER_ID_HEADER) String userId) {
    log.info("getMyInvestmentProduct: [userId: {}]", userId);

    return null;
  }
}
