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
import com.qiren.distributor.repository.OrderOutRepo;
import com.qiren.distributor.request.OrderInRequset;
import com.qiren.distributor.request.OrderOutRequest;

@Service
public class OrderService {
	
	@Autowired
	private OrderInRepository orderInRepository;
	@Autowired
	private OrderOutRepo orderOutRepo;
	
	public CommonResponse createOrderIn(OrderInRequset orderInRequset) {
		OrderIn orderIn = new OrderIn();
		String uidString = UUID.randomUUID().toString();
		// this has to be sent to manufacturer
		orderIn.setPkorderin(uidString);
		orderIn.setFkcustomer(orderInRequset.getCustomer());
		orderIn.setFkorder("N/A");
		orderIn.setFkseller(orderInRequset.getSeller());
		orderIn.setItemname(orderInRequset.getItemname());
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
	
	// find order in by company
}
