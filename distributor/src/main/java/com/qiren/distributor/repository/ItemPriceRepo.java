package com.qiren.distributor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.distributor.entities.Item;
import com.qiren.distributor.entities.ItemPrice;

@Repository
public interface ItemPriceRepo extends JpaRepository<ItemPrice, String> {

	List<ItemPrice> findByFkcompany(long fkcompany);

	List<ItemPrice> findByFkitem(Item fkitem);
}
