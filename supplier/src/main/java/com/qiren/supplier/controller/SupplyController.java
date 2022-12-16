package com.qiren.supplier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Logger;
import com.qiren.supplier.entities.Item;
import com.qiren.supplier.entities.Order;
import com.qiren.supplier.request.OrderRequest;
import com.qiren.supplier.request.ProductRequest;
import com.qiren.supplier.service.OrderService;
import com.qiren.supplier.service.ProductService;

@RestController
@RequestMapping("/api")
public class SupplyController {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/product/viewAll")
	public CommonResponse getAllItems() {
		List<Item> items = productService.getAllAItems();
		return CommonUtils.success(items);
	}

	@PostMapping("/price/create")
	public CommonResponse createItemPrice(
			@RequestBody ProductRequest request) {
		return productService.saveItemPrice(request);
	}

	@PostMapping("/price/byCompany/{companyId}")
	public CommonResponse findAllItemPriceByCompany(@PathVariable long companyId) {
		return productService.getPriceByCompany(companyId);
	}

	@PostMapping("/price/byItem/{itemId}")
	public CommonResponse findAllItemPriceByItem(@PathVariable long itemId) {
		return productService.getPriceByItem(itemId);
	}
	
	@PostMapping("/order/create")
	public CommonResponse createOrder(@RequestBody OrderRequest orderRequest) {
//		Logger.log(orderRequest.toString());
		try {
			return orderService.createOrder(orderRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("create failed");
		}
	}
	
	@PostMapping("/order/byCompany/{companyId}")
	public CommonResponse findAllOrderByCompany(@PathVariable String companyId) {
		return orderService.getOrderItemsByCompany(companyId);
	}
}
