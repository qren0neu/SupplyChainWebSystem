package com.qiren.distributor.request;

import lombok.Data;

@Data
public class ProductRequest {
	// no need for name here, single product
	private long company;
	private float price;
}
