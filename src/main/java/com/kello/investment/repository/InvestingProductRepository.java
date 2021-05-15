package com.kello.investment.repository;

import com.kello.investment.entity.InvestingProduct;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestingProductRepository extends CrudRepository<InvestingProduct, Long> {

//  @Query("SELECT P.totalInvestingAmount-(SELECT COLLAPSE(SUM(i.investAmount), 0) "
//      + "FROM InvestingStatus i WHERE i.productId = ?1) FROM InvestingProduct P "
//      + "WHERE P.productId = ?1")
//  Long getAvailableAmountByProductId(long productId);

  List<InvestingProduct> findAllByStartedAtLessThanAndAndFinishedAtGreaterThan(LocalDateTime now1,
      LocalDateTime now2);
}
