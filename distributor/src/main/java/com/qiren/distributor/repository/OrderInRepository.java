package com.qiren.distributor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qiren.distributor.entities.OrderIn;

public interface OrderInRepository extends JpaRepository<OrderIn, String> {

}
