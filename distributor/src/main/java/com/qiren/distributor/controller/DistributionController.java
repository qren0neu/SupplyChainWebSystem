package com.qiren.distributor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qiren.common.response.CommonResponse;
import com.qiren.distributor.request.ItemPriceRequest;
import com.qiren.distributor.request.OrderInRequset;
import com.qiren.distributor.service.OrderService;
import com.qiren.distributor.service.ProductService;

@RestController
@RequestMapping("/api")
public class DistributionController {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/price/create")
	public CommonResponse createItemPrice(@RequestBody ItemPriceRequest request) {
		return productService.createItemPrice(request);
	}

	@PostMapping("/price/byCompany/{companyId}")
	public CommonResponse findAllItemPriceByCompany(@PathVariable long companyId) {
		return productService.getPriceByCompany(companyId);
	}

	@PostMapping("/price/byItem/{itemId}")
	public CommonResponse findAllItemPriceByItem(@PathVariable long itemId) {
		return productService.getPriceByItem(itemId);
	}
	
	@PostMapping("/orderin/create")
	public CommonResponse createOrderIn(@RequestBody OrderInRequset orderInRequset) {
		return orderService.createOrderIn(orderInRequset);
	}
}
