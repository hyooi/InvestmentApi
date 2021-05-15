package com.kello.investment.repository;

import com.kello.investment.entity.InvestingStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface InvestingStatusRepository
    extends ReactiveCrudRepository<InvestingStatus, InvestingStatus.Key> {

}
