package com.qiren.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.Item;
import com.qiren.supplier.entities.ItemPrice;

@Repository
public interface ItemPriceRepo extends JpaRepository<ItemPrice, Long>{

	List<ItemPrice> findByFkCompany(long fkCompany);
	
	List<ItemPrice> findByFkItem(Item fkItem);
}
