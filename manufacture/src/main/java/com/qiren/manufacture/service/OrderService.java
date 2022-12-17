package com.qiren.manufacture.service;

import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OrderService {

	@PersistenceContext
	private EntityManager entityManager;

	public CommonResponse getAllAvailable() {
		String sqlString = "select oo.fkorderin, sum(oo.quantity) as 'total', sum(oi.quantity) as 'instock', (sum(oi.quantity) - sum(oo.quantity)) as 'remain', it.itemname as 'name' from supply_chain_distributor.order_out oo join supply_chain_distributor.order_in oi on oo.fkorderin = oi.pkorderin join supply_chain_distributor.item_price ip on oo.fkitemprice = ip.pkitemprice join supply_chain_distributor.item it on ip.fkitem = it.pkitem where oi.status = 'approved' group by oo.fkorderin, it.itemname having remain > 0;";
		return CommonUtils.success(CommonUtils.simpleSql(entityManager, sqlString));
	}

}
