package com.qiren.supplier.request;

import java.util.Date;
import java.util.List;

import com.qiren.supplier.entities.ItemPrice;

import lombok.Data;

@Data
public class OrderRequest {
	
	private Order order;
	private List<OrderItem> orderItems;
	
	@Data
	public static class Order {

	    private Date startdate;
	    private int esttime;
	    private int seller;
	    private int customer;
	}

	@Data
	public static class OrderItem {

	    private long itemPrice;
	    private int quantity;
	}
}
