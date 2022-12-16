package com.qiren.supplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.supplier.entities.Item;
import com.qiren.supplier.entities.ItemPrice;
import com.qiren.supplier.repository.ItemPriceRepo;
import com.qiren.supplier.repository.ItemRepo;
import com.qiren.supplier.request.ProductRequest;

import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ItemRepo itemRepo;
	@Autowired
	private ItemPriceRepo itemPriceRepo;
	
	public List<Item> getAllAItems() {
		return itemRepo.findAll();
	}
	
	public CommonResponse saveItemPrice(ProductRequest request) {
		
		Item item = new Item();
		item.setPkItem(request.getItem());
		
		ItemPrice itemPrice = new ItemPrice();
		itemPrice.setFkItem(item);
		itemPrice.setInStock(request.getInstock());
		itemPrice.setPrice(request.getPrice());
		itemPrice.setStatus(request.getStatus());
		itemPrice.setUnit(request.getUnit());
		itemPrice.setFkCompany(request.getCompany());
		
		try {
			itemPriceRepo.save(itemPrice);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("create failed");
		}
		return CommonUtils.success();
	}
	
	public CommonResponse getPriceByCompany(long companyId) {
		try {
			 List<ItemPrice> itemPrices = itemPriceRepo.findByFkCompany(companyId);
			 return CommonUtils.success(itemPrices);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("cannot get");
		}
	}
	
	public CommonResponse getPriceByItem(long itemId) {
		Item item = new Item();
		item.setPkItem(itemId);
		try {
			 List<ItemPrice> itemPrices = itemPriceRepo.findByFkItem(item);
			 return CommonUtils.success(itemPrices);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("cannot get");
		}
	}
}
