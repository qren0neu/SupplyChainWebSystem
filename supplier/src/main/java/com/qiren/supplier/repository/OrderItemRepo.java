package com.qiren.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.OrderItem;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, String> {

	@Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.fkseller = :sellerId")
	List<OrderItem> findOrderItemsBySeller(String sellerId);
}
