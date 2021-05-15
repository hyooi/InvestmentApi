package com.kello.investment.repository;

import com.kello.investment.entity.InvestingProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface InvestingProductRepository extends ReactiveCrudRepository<InvestingProduct, Long> {

  @Query("SELECT i FROM InvestingProduct i")
  Flux<InvestingProduct> findAll();
}
