package com.kello.investment.repository;

import com.kello.investment.dto.InvestingProductDto;
import com.kello.investment.entity.InvestingProduct;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestingProductRepository extends CrudRepository<InvestingProduct, Long> {

  @Query("SELECT new com.kello.investment.dto.InvestingProductDto(A.productId, A.title, "
      + "A.totalInvestingAmount, A.startedAt, A.finishedAt,"
      + "COALESCE(SUM(B.investAmount), 0) AS presentInvestingAmount, COUNT(B.userId) AS investorCnt) "
      + "FROM InvestingProduct A LEFT JOIN A.status B "
      + "WHERE A.startedAt < ?1 AND A.finishedAt > ?1 "
      + "GROUP BY A.productId")
  List<InvestingProductDto> findAllProduct(LocalDateTime now);

  @Query("SELECT A.totalInvestingAmount < COALESCE(SUM(B.investAmount), 0) + ?2 "
      + "FROM InvestingProduct A LEFT JOIN A.status B "
      + "WHERE A.productId=?1 "
      + "GROUP BY A.productId")
  boolean isExceedAmount(long productId, long amount);
}
