package com.qiren.supplier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qiren.supplier.entities.OrderInventory;

public interface OrderInventoryRepo extends JpaRepository<OrderInventory, Long> {

	@Query(value = "select oo.fkorderin, sum(oo.quantity) as 'total', \n"
			+ "sum(oi.quantity) as 'instock', \n"
			+ "(sum(oi.quantity) - sum(oo.quantity)) as 'remain', \n"
			+ "it.itemname as 'name' \n"
			+ "from supply_chain_distributor.order_out oo \n"
			+ "join supply_chain_distributor.order_in oi \n"
			+ "on oo.fkorderin = oi.pkorderin\n"
			+ "join supply_chain_distributor.item_price ip\n"
			+ "on oo.fkitemprice = ip.pkitemprice \n"
			+ "join supply_chain_distributor.item it\n"
			+ "on ip.fkitem = it.pkitem\n"
			+ "where oi.status = 'approved' \n"
			+ "group by oo.fkorderin, it.itemname\n"
			+ "having remain > 0;", nativeQuery = true)
	List<OrderInventory> findOrderInventory();
	
	@Query(value = "select oo.fkorderin, sum(oo.quantity) as 'total', \n"
			+ "sum(oi.quantity) as 'instock', \n"
			+ "(sum(oi.quantity) - sum(oo.quantity)) as 'remain', \n"
			+ "it.itemname as 'name',\n"
			+ "oi.fkcustomer as 'company'  \n"
			+ "from supply_chain_distributor.order_out oo \n"
			+ "join supply_chain_distributor.order_in oi \n"
			+ "on oo.fkorderin = oi.pkorderin\n"
			+ "join supply_chain_distributor.item_price ip\n"
			+ "on oo.fkitemprice = ip.pkitemprice \n"
			+ "join supply_chain_distributor.item it\n"
			+ "on ip.fkitem = it.pkitem\n"
			+ "where oi.status = 'approved' \n"
			+ "group by oo.fkorderin, it.itemname\n"
			+ "having remain > ?1 and company = ?2;", nativeQuery = true)
	List<OrderInventory> findOrderInventoryByRemainAndCompany(String remain, String company);
}
