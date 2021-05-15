package com.kello.investment.dto;

import com.kello.investment.entity.InvestingProduct;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class InvestingProductDto {

  private Long productId;
  private String title;
  private Long totalInvestingAmount;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  private Long presentInvestingAmount;
  private Long investingUserCnt;
  private String recruitingStatus;
  private String recruitingPeriod;

  public static InvestingProductDto from(InvestingProduct entity) {
    val dto = new InvestingProductDto();
    BeanUtils.copyProperties(entity, dto);

    return dto;
  }
}
