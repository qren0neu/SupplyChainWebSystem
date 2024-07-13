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
import com.qiren.supplier.entities.Item;
import com.qiren.supplier.entities.UserAuthEntity;
import com.qiren.supplier.request.OrderRequest;
import com.qiren.supplier.request.ProductRequest;
import com.qiren.supplier.request.RequestWrapper;
import com.qiren.supplier.service.OrderService;
import com.qiren.supplier.service.ProductService;
import com.qiren.supplier.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Deprecated
@RestController
@RequestMapping("/api")
public class SupplyController {

	@Autowired
	private ProductService productService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
	@PostMapping("/product/viewAll")
	public CommonResponse getAllItems() {
		List<Item> items = productService.getAllAItems();
		return CommonUtils.success(items);
	}

	@PostMapping("/price/create")
	public CommonResponse createItemPrice(
			HttpServletRequest servletRequest,
			@RequestBody RequestWrapper request) {
		
		UserAuthEntity userAuthEntity = userService.getAuth(servletRequest);
		
		int company = userAuthEntity.getFkcompany();
		
		return productService.saveItemPrice(request.getData(), company);
	}

	@PostMapping("/price/byCompany/{companyId}")
	public CommonResponse findAllItemPriceByCompany(@PathVariable long companyId) {
		return productService.getPriceByCompany(companyId);
	}

	@PostMapping("/price/byCompany")
	public CommonResponse findAllItemPriceByCompany2(
			HttpServletRequest servletRequest) {

		UserAuthEntity userAuthEntity = userService.getAuth(servletRequest);
		
		int company = userAuthEntity.getFkcompany();
		
		return productService.getPriceByCompany(company);
	}

	@PostMapping("/price/byItem/{itemId}")
	public CommonResponse findAllItemPriceByItem(@PathVariable long itemId) {
		return productService.getPriceByItem(itemId);
	}
	
	@PostMapping("/order/create")
	public CommonResponse createOrder(@RequestBody OrderRequest orderRequest) {
		try {
			return orderService.createOrder(orderRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("create failed");
		}
	}
	
	@PostMapping("/order/byCompany/{companyId}")
	public CommonResponse findAllOrderByCompany(
			@PathVariable String companyId) {
		
//		UserAuthEntity userAuthEntity = userService.getAuth(servletRequest);
//		
//		int company = userAuthEntity.getFkcompany();
		
		return orderService.getOrderItemsByCompany(companyId);
	}
	
	@PostMapping("/order/byCompany")
	public CommonResponse findAllOrderByCompany2(
			HttpServletRequest servletRequest) {
		
		UserAuthEntity userAuthEntity = userService.getAuth(servletRequest);
		
		int company = userAuthEntity.getFkcompany();
		
		return orderService.getOrderItemsByCompany(company + "");
	}
	
//	@PostMapping("/order/byCompan")
//	public CommonResponse findAllOrderByCompany(@PathVariable String companyId) {
//		return orderService.getOrderItemsByCompany(companyId);
//	}
	
	@PostMapping("/product/available/{itemtype}/{quan}")
	public CommonResponse findAllAvailable(@PathVariable String itemtype, @PathVariable String quan) {
		String sqlString = "SELECT * FROM supply_chain_supplier.item_price where fkItem = ? and instock > ?	; ";
		
		// pk, fkitm, company, price, unit, instock, status
		
		return productService.getAvailable(sqlString, itemtype, quan);
	}
	
	@PostMapping("/product/availableByCompany/{itemtype}/{quan}")
	public CommonResponse findAllAvailableByCompany(
			HttpServletRequest servletRequest,
			@PathVariable String itemtype, @PathVariable String quan) {
		String sqlString = "SELECT * FROM supply_chain_supplier.item_price where fkItem = ? and instock > ? and fkcompany = ?; ";

		UserAuthEntity userAuthEntity = userService.getAuth(servletRequest);
		
		int company = userAuthEntity.getFkcompany();
		
		String companyString = company + "";
		
		return productService.getAvailable(sqlString, itemtype, quan, companyString);
	}
}
