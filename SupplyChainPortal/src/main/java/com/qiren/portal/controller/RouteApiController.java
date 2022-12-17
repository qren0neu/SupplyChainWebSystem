package com.qiren.portal.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.Logger;
import com.qiren.common.tools.RestManager;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.CompanyUserEntity;
import com.qiren.portal.repository.CompanyUserRepository;
import com.qiren.portal.service.CompanyService;
import com.qiren.portal.service.LoginService;
import com.qiren.portal.service.RouteService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/route")
public class RouteApiController {

	@Autowired
	private RouteService routeService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CompanyUserRepository companyUserRepository;
	@Autowired
	private EntityManager entityManager;

	@PostMapping("/viewAllService")
	public CommonResponse getAllServices(HttpServletRequest request) {
		if (!loginService.isLogin(request)) {
			return CommonUtils.fail("Not login");
		}
		return CommonUtils.successOrFail(routeService.getAllServices());
	}

	@PostMapping("/buildRoute")
	public CommonResponse buildRoute(HttpServletRequest servletRequest,
			@RequestBody HashMap<String, Object> buildRouteRequest) {
		if (!loginService.isLogin(servletRequest)) {
			return CommonUtils.fail("Not login");
		}
		return CommonUtils.successOrFail(routeService.buildRoute(servletRequest, buildRouteRequest));
	}

	@PostMapping("/listSupplier")
	public CommonResponse listSupplier(HttpServletRequest request, @RequestBody HashMap<String, Object> requestParams) {
//		if (!loginService.isLogin(request)) {
//			return CommonUtils.fail("Not login");
//		}

		UserBean userBean = loginService.getLoginUser(request);

		if (null == userBean) {
			return CommonUtils.fail("Not login");
		}

		Logger.log("pass1");

		String itemTypeString = requestParams.get("itemtype").toString();
		String quantityString = requestParams.get("quan").toString();

		String urlString = Constants.URL_SUPPLIER + "/api/product/available";

		urlString += ("/" + itemTypeString);
		urlString += ("/" + quantityString);

		CommonResponse companyList = RestManager.getInstance().sendHttpPost(restTemplate, urlString, requestParams);

		String companyListStr = companyList.getData();

		Type listType = new TypeToken<List<List<Float>>>() {
		}.getType();
		List<List<Float>> listCompany = new Gson().fromJson(companyListStr, listType);

		Logger.log("pass2");

		List<Integer> idList = new ArrayList<>();

		for (List<Float> innerList : listCompany) {
			// get fkcompany
			idList.add((int) innerList.get(2).floatValue());
		}

		String sqlIn = "(" + String.join(",", idList.stream().map(Object::toString).collect(Collectors.toList())) + ")";

		String routeSqlString = "SELECT ro.pkroute 'route', ro.esttime 'est', sv.factor 'factor1', sv.costper 'price', rc.fkcompany FROM supply_chain_portal.route ro join service sv on ro.fkService = sv.pkService join route_company rc on rc.fkroute = ro.pkroute where ro.pkRoute in (select fkroute from supply_chain_portal.route_company rc where rc.fkcompany = ?) and rc.fkcompany in "
				+ sqlIn;

		CompanyUserEntity companyUserEntity = companyUserRepository.findByUser(userBean.getCommonInfo());

		long companyId = companyUserEntity.getCompany().getPkCompany();

		Query query = entityManager.createNativeQuery(routeSqlString);

		query.setParameter(1, companyId);

		List<Object[]> resultList = query.getResultList();

		List<List<String>> temp = new ArrayList<>();

		for (List<Float> innerList : listCompany) {
			for (Object[] array : resultList) {
				// company match
				if ((int) innerList.get(2).floatValue() == ((Number) array[4]).intValue()) {
					// found a match
					System.out.println("Match found: " + innerList.get(2) + "," + array[4]);
					List<String> tempList = new ArrayList<>();
					tempList.add(array[0].toString());
					tempList.add(array[1].toString());
					tempList.add(array[2].toString());
					tempList.add(array[3].toString());
					tempList.add(array[4].toString());
					for (Float f : innerList) {
						tempList.add(f.toString());
					}
					temp.add(tempList);
				}
			}
		}

		return CommonUtils.success(temp);

//		return CommonUtils.success(CommonUtils.simpleSql(routeSqlString, entityManager, companyId));
	}

	@PostMapping("/listSupplier/test")
	public CommonResponse listSupplierTest(HttpServletRequest request,
			@RequestBody HashMap<String, Object> requestParams) {

		CommonUserEntity commonUserEntity = loginService.findUserInfoByName(CommonUtils.md5("renqi1118"));

		String itemTypeString = requestParams.get("itemtype").toString();
		String quantityString = requestParams.get("quan").toString();

		String urlString = Constants.URL_SUPPLIER + "/api/product/available";

		urlString += ("/" + itemTypeString);
		urlString += ("/" + quantityString);

		CommonResponse companyList = RestManager.getInstance().sendHttpPost(restTemplate, urlString, requestParams);

		String companyListStr = companyList.getData();

		Type listType = new TypeToken<List<List<Float>>>() {
		}.getType();
		List<List<Float>> listCompany = new Gson().fromJson(companyListStr, listType);

		List<Integer> idList = new ArrayList<>();

		for (List<Float> innerList : listCompany) {
			idList.add((int) innerList.get(2).floatValue());
		}

		String sqlIn = "(" + String.join(",", idList.stream().map(Object::toString).collect(Collectors.toList())) + ")";

		String routeSqlString = "SELECT ro.pkroute 'route', ro.esttime 'est', sv.factor 'factor1', sv.costper 'price', rc.fkcompany FROM supply_chain_portal.route ro join service sv on ro.fkService = sv.pkService join route_company rc on rc.fkroute = ro.pkroute where ro.pkRoute in (select fkroute from supply_chain_portal.route_company rc where rc.fkcompany = ?) and rc.fkcompany in "
				+ sqlIn;

		CompanyUserEntity companyUserEntity = companyUserRepository.findByUser(commonUserEntity);

//		long companyId = companyUserEntity.getCompany().getPkCompany();
		long companyId = 14L;

		Query query = entityManager.createNativeQuery(routeSqlString);

		query.setParameter(1, companyId);

		List<Object[]> resultList = query.getResultList();

		for (List<Float> innerList : listCompany) {
			System.out.print("Interating");
			for (Object[] array : resultList) {
				System.out.println((int) innerList.get(2).floatValue() + " " + ((Number) array[4]).intValue());
				if ((int) innerList.get(2).floatValue() == ((Number) array[4]).intValue()) {
					// found a match
					System.out.println("Match found: " + innerList.get(2) + "," + array[4]);
				}
			}
		}

//		return CommonUtils.success();

		return CommonUtils.success(CommonUtils.simpleSql(routeSqlString, entityManager, companyId));
	}
}
