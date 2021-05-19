package com.kello.investment.dto;

import com.kello.investment.entity.InvestingStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
public class InvestingStatusDto {

  private long productId;
  private long userId;
  private long investAmount;
  private LocalDateTime investDate;

  public static InvestingStatusDto of(InvestingStatus entity) {
    var dto = new InvestingStatusDto();
    BeanUtils.copyProperties(entity, dto);

    return dto;
  }
}
