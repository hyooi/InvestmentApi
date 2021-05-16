package com.kello.investment.controller.api;

import com.kello.investment.dto.CommonResponse;
import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.service.InvestingService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class InvestingController {

  private static final String USER_ID_HEADER = "X-USER-ID";
  private final InvestingService service;

  public InvestingController(InvestingService service) {
    this.service = service;
  }

  @GetMapping("/products")
  @ResponseStatus(HttpStatus.CREATED)
  public CommonResponse<List<InvestingProductDto>> getAllInvestmentProducts() {
    log.info("getAllInvestmentProducts");

    return service.getAllInvestmentProducts();
  }

  @PostMapping("/invest/{productId}/{amount}")
  public CommonResponse<Object> invest(@RequestHeader(USER_ID_HEADER) String userId,
      @PathVariable("productId") long productId,
      @PathVariable("amount") long amount) {
    log.info("invest request: [userId: {}, productId: {}, amount: {}]", userId, productId, amount);
    return service.invest(userId, productId, amount);
  }

  @GetMapping("/product")
  public CommonResponse<List<MyInvestingProductDto>> getMyInvestmentProduct(
      @RequestHeader(USER_ID_HEADER) String userId) {
    log.info("getMyInvestmentProduct: [userId: {}]", userId);

    return service.getMyInvestmentProduct(userId);
  }
}
