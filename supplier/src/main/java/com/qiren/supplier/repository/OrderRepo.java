package com.qiren.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {

//	@Query(value = "insert into orders (esttime, fkcustomer, fkseller, startdate) values (?1, ?2, ?3, ?4)", nativeQuery = true)
//	Order saveOrder(Order order);
}
