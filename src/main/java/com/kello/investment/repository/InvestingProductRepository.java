package com.kello.investment.repository;

import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.entity.InvestingProduct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestingProductRepository extends CrudRepository<InvestingProduct, Long> {

  @Query("SELECT new com.kello.investment.dto.InvestingProductDto(A.productId, A.title, "
      + "A.totalInvestingAmount, A.startedAt, A.finishedAt,"
      + "A.presentInvestingAmount, COUNT(B.userId) AS investorCnt) "
      + "FROM InvestingProduct A LEFT JOIN A.status B "
      + "WHERE A.startedAt <= ?1 AND A.finishedAt > ?1 "
      + "GROUP BY A.productId")
  List<InvestingProductDto> findAllValidProduct(LocalDateTime now);

  @Query("SELECT A.startedAt <= ?2 AS validStartTime, A.finishedAt > ?2 AS validEndTime "
      + "FROM InvestingProduct A WHERE A.productId = ?1")
  Map<String, Boolean> getComparedTimes(long productId, LocalDateTime timeZone);

  @Query("SELECT A.totalInvestingAmount < A.presentInvestingAmount + ?2 "
      + "FROM InvestingProduct A "
      + "WHERE A.productId=?1 "
      + "GROUP BY A.productId")
  boolean isExceedAmount(long productId, long amount);

}
