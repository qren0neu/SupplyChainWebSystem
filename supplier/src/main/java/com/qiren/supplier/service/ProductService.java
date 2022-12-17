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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ItemRepo itemRepo;
	@Autowired
	private ItemPriceRepo itemPriceRepo;
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Item> getAllAItems() {
		return itemRepo.findAll();
	}
	
	public CommonResponse saveItemPrice(List<ProductRequest> requests, int company) {
		
		List<ItemPrice> itemPrices = new ArrayList<>();
		
		for (ProductRequest request : requests) {
			Item item = new Item();
			item.setPkItem(request.getItem());
			
			ItemPrice itemPrice = new ItemPrice();
			
			if (request.getId() > 0) {
				itemPrice.setPkItemPrice(request.getId());
			}
			
			itemPrice.setFkItem(item);
			itemPrice.setInStock(request.getInstock());
			itemPrice.setPrice(request.getPrice());
			itemPrice.setStatus(request.getStatus());
			itemPrice.setUnit(request.getUnit());
			itemPrice.setFkCompany(company);
			itemPrices.add(itemPrice);
		}
		
		try {
			itemPriceRepo.saveAll(itemPrices);
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
	
	public CommonResponse getAvailable(String sql, String type, String quan) {
		return CommonUtils.success(CommonUtils.simpleSql(sql, entityManager, type, quan));
	}
	
	public CommonResponse getAvailable(String sql, String type, String quan, String company) {
		return CommonUtils.success(CommonUtils.simpleSql(sql, entityManager, type, quan, company));
	}
}
