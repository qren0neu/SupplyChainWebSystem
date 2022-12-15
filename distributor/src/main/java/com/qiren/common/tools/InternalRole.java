package com.qiren.common.tools;

public class InternalRole {

	public static class Customer {
		public static final String CUSTOMER = "Customer_CUS";
	}
	
	public static class Supplier {
		public static final String COMPANY_MANAGER = "CompanyManager_SUP";
		public static final String PRODUCT_MANAGER = "ProductManager_SUP";
		public static final String ORDER_MANAGER = "OrderManager_SUP";
	}
	
	public static class Distributor {
		public static final String COMPANY_MANAGER = "CompanyManager_DIS";
		public static final String PRODUCT_MANAGER = "ProductManager_DIS";
		public static final String ORDER_MANAGER = "OrderManager_DIS";
	}
	
	public static class Manufacturer {

		public static final String COMPANY_MANAGER = "CompanyManager_MAU";
		public static final String PRODUCT_MANAGER = "ProductManager_MAU";
		public static final String ORDER_MANAGER = "OrderManager_MAU";
	}
	
	public static class Router {
		public static final String ROUTE_PLANNER = "RouterPlanner_ROU";
	}
	
	public static class Deliver {
		public static final String COURIER = "Courier_DEL";
	}
}
