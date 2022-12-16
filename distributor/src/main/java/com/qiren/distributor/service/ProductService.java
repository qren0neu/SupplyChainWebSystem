package com.qiren.distributor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.distributor.entities.Item;
import com.qiren.distributor.entities.ItemPrice;
import com.qiren.distributor.repository.ItemPriceRepo;
import com.qiren.distributor.request.ItemPriceRequest;

@Service
public class ProductService {
	
	@Autowired
	private ItemPriceRepo itemPriceRepo;
	
	public CommonResponse createItemPrice(ItemPriceRequest itemPriceRequest) {
		ItemPrice itemPrice = new ItemPrice();
		itemPrice.setFkcompany(itemPriceRequest.getCompany());
		itemPrice.setPrice(itemPriceRequest.getPrice());
		
		Item item = new Item();
		item.setPkitem(itemPriceRequest.getItem());
		
		itemPrice.setFkitem(item);
		
		try {
			itemPriceRepo.save(itemPrice);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Create item price failed");
		}
		return CommonUtils.success();
	}
	
	public CommonResponse getPriceByCompany(long companyId) {
		try {
			 List<ItemPrice> itemPrices = itemPriceRepo.findByFkcompany(companyId);
			 return CommonUtils.success(itemPrices);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("cannot get");
		}
	}
	
	public CommonResponse getPriceByItem(long itemId) {
		Item item = new Item();
		item.setPkitem(itemId);
		try {
			 List<ItemPrice> itemPrices = itemPriceRepo.findByFkitem(item);
			 return CommonUtils.success(itemPrices);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("cannot get");
		}
	}
}
