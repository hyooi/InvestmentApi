package com.kello.investment.repository;

import com.kello.investment.dto.MyInvestingProductDto;
import com.kello.investment.entity.InvestingStatus;
import com.kello.investment.entity.InvestingStatus.Key;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InvestingStatusRepository
    extends CrudRepository<InvestingStatus, Key> {

  @Query("SELECT new com.kello.investment.dto.MyInvestingProductDto(A.productId, B.title, "
      + "B.totalInvestingAmount, A.investAmount AS myInvestAmount, A.investDate) "
      + "FROM InvestingStatus A LEFT JOIN A.investProduct B "
      + "WHERE A.userId = ?1")
  List<MyInvestingProductDto> findAllByUserId(long userId);
}