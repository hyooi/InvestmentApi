package com.kello.investment.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(InvestingStatus.Key.class)
public class InvestingStatus {

  @Id
  private long productId;

  @Id
  private long userId;

  private long investAmount;
  private LocalDateTime investDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productId", updatable = false, insertable = false)
  private InvestingProduct investProduct;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  public static class Key implements Serializable {

    @Id
    private long productId;

    @Id
    private long userId;
  }

}
