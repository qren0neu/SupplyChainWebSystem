package com.qiren.distributor.request;

import java.util.Date;

import lombok.Data;

@Data
public class OrderInRequset {

	private String itemname;
	
	private String status;
	
	private int quantity;
	
	private int price;
	
	private Date startdate;
	
    private int esttime;

    // fks
    
    private long seller;

    private String order;

    private long customer;
}
