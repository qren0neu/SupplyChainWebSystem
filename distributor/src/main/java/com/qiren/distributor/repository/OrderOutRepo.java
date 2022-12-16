package com.qiren.distributor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.distributor.entities.OrderOut;

@Repository
public interface OrderOutRepo extends JpaRepository<OrderOut, String>{

}
