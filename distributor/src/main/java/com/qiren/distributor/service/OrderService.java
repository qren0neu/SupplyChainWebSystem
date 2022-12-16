package com.qiren.distributor.service;

import org.springframework.stereotype.Service;

import com.qiren.distributor.entities.ItemPrice;
import com.qiren.distributor.entities.OrderIn;
import com.qiren.distributor.entities.OrderOut;
import com.qiren.distributor.request.OrderOutRequest;

@Service
public class OrderService {
	
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
		orderOut.setStartdate(orderIn.getStartdate());
		orderOut.setStatus(orderIn.getStatus());
		
		return orderOut;
	}
}
