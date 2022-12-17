package com.qiren.distributor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qiren.distributor.entities.OrderInventory;

public interface OrderInventoryRepo extends JpaRepository<OrderInventory, Long> {

	@Query(value = "select oo.fkorderin, sum(oo.quantity) as 'total',  "
			+ "sum(oi.quantity) as 'instock',  "
			+ "(sum(oi.quantity) - sum(oo.quantity)) as 'remain',  "
			+ "it.itemname as 'name'  "
			+ "from supply_chain_distributor.order_out oo  "
			+ "join supply_chain_distributor.order_in oi  "
			+ "on oo.fkorderin = oi.pkorderin "
			+ "join supply_chain_distributor.item_price ip "
			+ "on oo.fkitemprice = ip.pkitemprice  "
			+ "join supply_chain_distributor.item it "
			+ "on ip.fkitem = it.pkitem "
			+ "where oi.status = 'approved'  "
			+ "group by oo.fkorderin, it.itemname "
			+ "having remain > 0;", nativeQuery = true)
	List<OrderInventory> findOrderInventory();
	
	@Query(value = "select oo.fkorderin, sum(oo.quantity) as 'total',  "
			+ "sum(oi.quantity) as 'instock',  "
			+ "(sum(oi.quantity) - sum(oo.quantity)) as 'remain',  "
			+ "it.itemname as 'name', "
			+ "oi.fkcustomer as 'company'   "
			+ "from supply_chain_distributor.order_out oo  "
			+ "join supply_chain_distributor.order_in oi  "
			+ "on oo.fkorderin = oi.pkorderin "
			+ "join supply_chain_distributor.item_price ip "
			+ "on oo.fkitemprice = ip.pkitemprice  "
			+ "join supply_chain_distributor.item it "
			+ "on ip.fkitem = it.pkitem "
			+ "where oi.status = 'approved'  "
			+ "group by oo.fkorderin, it.itemname "
			+ "having remain > ?1 and company = ?2;", nativeQuery = true)
	List<OrderInventory> findByRemainAndCompany(int remain, int company);
}
