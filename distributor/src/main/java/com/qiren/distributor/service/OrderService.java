package com.qiren.distributor.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.distributor.entities.ItemPrice;
import com.qiren.distributor.entities.OrderIn;
import com.qiren.distributor.entities.OrderOut;
import com.qiren.distributor.repository.OrderInRepository;
import com.qiren.distributor.repository.OrderInventoryRepo;
import com.qiren.distributor.repository.OrderOutRepo;
import com.qiren.distributor.request.OrderInRequset;
import com.qiren.distributor.request.OrderOutRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class OrderService {

	@Autowired
	private OrderInRepository orderInRepository;
	@Autowired
	private OrderOutRepo orderOutRepo;
	@Autowired
	private OrderInventoryRepo orderInvRepo;
	@PersistenceContext
	private EntityManager entityManager;

	public CommonResponse createOrderIn(OrderInRequset orderInRequset) {
		OrderIn orderIn = new OrderIn();
		String uidString = UUID.randomUUID().toString();
		// this has to be sent to manufacturer
		orderIn.setPkorderin(uidString);
		orderIn.setFkcustomer(orderInRequset.getCustomer());
		orderIn.setFkorder("N/A");
		orderIn.setFkseller(orderInRequset.getSeller());
		orderIn.setItemname("N/A");
//		orderIn.setItemname(orderInRequset.getItemname());
		orderIn.setPrice(orderInRequset.getPrice());
		orderIn.setQuantity(orderInRequset.getQuantity());
		orderIn.setEsttime(orderInRequset.getEsttime());
		orderIn.setStartdate(new Date());
		// pending, approved, returned
		orderIn.setStatus("pending");

		try {
			orderInRepository.save(orderIn);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("create order failed");
		}

		// send a request to manu

		return CommonUtils.success();
	}

	private OrderOut transferOrderOut(OrderOutRequest orderOutRequest) {
		OrderOut orderOut = new OrderOut();
		orderOut.setEsttime(orderOutRequest.getEsttime());
		orderOut.setFkcustomer(orderOutRequest.getCustomer());
		ItemPrice iPrice = new ItemPrice();
		iPrice.setPkitemprice(orderOutRequest.getItemprice());
		orderOut.setFkitemprice(iPrice);

		OrderIn orderIn = new OrderIn();
		orderIn.setPkorderin(orderOutRequest.getOrderin());
		orderOut.setFkorderin(orderIn);
		orderOut.setQuantity(orderOutRequest.getQuantity());
		orderOut.setStartdate(orderOutRequest.getStartdate());
		// pending, approved, returned
		orderOut.setStatus("pending");
//		orderOut.setStatus(orderOutRequest.getStatus());

		return orderOut;
	}

	// find order out by company and status
	public CommonResponse viewOrders(long company) {
		String sqlString = "select pkorderout 'id', fkorderin 'batch', oo.status 'status', it.itemname 'name', ip.price 'price', oo.quantity 'quantity' from supply_chain_distributor.order_out oo join supply_chain_distributor.item_price ip  on oo.fkitemprice = ip.pkitemprice join supply_chain_distributor.item it on ip.fkitem = it.pkitem where ip.fkcompany = ?;";

		Query query = entityManager.createNativeQuery(sqlString);

		query.setParameter(1, company);
		
		try {
			return CommonUtils.success(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("failed to get");
		}
	}
	// find order in by company

	public CommonResponse getAvailableItemFromCompany(int quan, long companyId) {
		try {
			String sqlString = "select oo.fkorderin, sum(oo.quantity) as 'total',  "
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
					+ "having remain > ? and company = ?;";
			
			Query query = entityManager.createNativeQuery(sqlString);
			
			query.setParameter(1, quan);
			query.setParameter(2, companyId);
			
			return CommonUtils.success(query.getResultList());
//			return CommonUtils.success(orderInvRepo.findByRemainAndCompany(quan, ((int)companyId)));
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Failed to get");
			// TODO: handle exception
		}
	}

	public CommonResponse getInStockFromCompany(long companyId) {
		try {
			String sqlString = "select it.pkitem as 'id', sum(oo.quantity) as 'total',  "
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
					+ "group by it.pkitem, it.itemname, oi.fkcustomer "
					+ "having company = ?;";
			
			Query query = entityManager.createNativeQuery(sqlString);
			
			query.setParameter(1, companyId);
			
			return CommonUtils.success(query.getResultList());
//			return CommonUtils.success(orderInvRepo.findByRemainAndCompany(quan, ((int)companyId)));
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Failed to get");
			// TODO: handle exception
		}
	}
	
	@Deprecated
	public CommonResponse getInStockByBatchFromCompany(long companyId) {
		// adding group by
		try {
			String sqlString = "select oo.fkorderin, sum(oo.quantity) as 'total',  "
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
					+ "having company = ?;";
			
			Query query = entityManager.createNativeQuery(sqlString);
			
			query.setParameter(1, companyId);
			
			return CommonUtils.success(query.getResultList());
//			return CommonUtils.success(orderInvRepo.findByRemainAndCompany(quan, ((int)companyId)));
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Failed to get");
			// TODO: handle exception
		}
	}
}
