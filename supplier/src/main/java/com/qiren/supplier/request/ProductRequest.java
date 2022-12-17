package com.qiren.supplier.request;

import lombok.Data;

@Data
public class ProductRequest {
	private long id;
	private long item;
	private long company;
	private float price;
	private int unit;
	private int instock;
	private int status;
}
