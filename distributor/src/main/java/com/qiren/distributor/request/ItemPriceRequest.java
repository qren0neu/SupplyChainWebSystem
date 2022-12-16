package com.qiren.distributor.request;

import lombok.Data;

@Data
public class ItemPriceRequest {
	private float price;
	private long company;
	private long item;
}
