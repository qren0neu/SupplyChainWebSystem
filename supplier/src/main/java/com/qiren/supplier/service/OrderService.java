package com.qiren.supplier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Logger;
import com.qiren.supplier.entities.ItemPrice;
import com.qiren.supplier.entities.Order;
import com.qiren.supplier.entities.OrderItem;
import com.qiren.supplier.repository.OrderItemRepo;
import com.qiren.supplier.repository.OrderRepo;
import com.qiren.supplier.request.OrderRequest;

@Service
public class OrderService {
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private OrderItemRepo orderItemRepo;

	public CommonResponse createOrder(OrderRequest orderRequest) {
		OrderRequest.Order order = orderRequest.getOrder();
		List<OrderRequest.OrderItem> orderItems = orderRequest.getOrderItems();
		
		Order orderEntity = new Order();
		orderEntity.setEsttime(order.getEsttime());
		orderEntity.setStartdate(order.getStartdate());
		orderEntity.setFkcustomer(order.getCustomer());
		orderEntity.setFkseller(order.getSeller());
		
		String pkOrderString = UUID.randomUUID().toString();
		orderEntity.setPkorder(pkOrderString);
		
		Logger.log("Step 1");
		Order createdOrder = orderRepo.save(orderEntity);

		Logger.log("Step 2" + createdOrder.getPkorder());
		List<OrderItem> entityOrderItems = new ArrayList<>();
		for (OrderRequest.OrderItem item : orderItems) {
			OrderItem orderItem = new OrderItem();
			
			ItemPrice itemPrice = new ItemPrice();
			itemPrice.setPkItemPrice(item.getItemPrice());
			
			orderItem.setItemPrice(itemPrice);
			orderItem.setOrder(createdOrder);
			orderItem.setQuantity(item.getQuantity());
			
			entityOrderItems.add(orderItem);
		}
		
		orderItemRepo.saveAll(entityOrderItems);
		return CommonUtils.success();
	}
	
	public CommonResponse getOrderItemsByCompany(String sellerId) {
		List<OrderItem> orderItems = orderItemRepo.findOrderItemsBySeller(sellerId);
		return CommonUtils.success(orderItems);
	}
}
