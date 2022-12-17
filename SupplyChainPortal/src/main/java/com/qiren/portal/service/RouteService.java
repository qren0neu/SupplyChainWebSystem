package com.qiren.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.entities.CompanyUserEntity;
import com.qiren.portal.repository.CompanyUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class RouteService {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private CompanyUserRepository companyUserRepository;
	@Autowired
	private LoginService loginService;

	public Object getAllServices() {
		String sqlString = "select ts.fkService 'service', group_concat(tr.type) 'trans', avg(sv.factor) 'factor', avg(sv.costper) 'price' from trans_service ts join service sv on ts.fkService = sv.pkService join trans tr on ts.fkTrans = tr.pkTrans group by ts.fkService;";
		try {
			return CommonUtils.simpleSql(entityManager, sqlString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public boolean buildRoute(HttpServletRequest request, HashMap<String, Object> buildRouteRequest) {
		String routeIdString = UUID.randomUUID().toString();
		String sqlString1 = "insert into supply_chain_portal.route values (?, ?, ?)";

		String estdate = buildRouteRequest.get("est").toString();
		String companyString = buildRouteRequest.get("company").toString();
		String serviceString = buildRouteRequest.get("service").toString();

//		Object[] 
		try {
			CommonUtils.simpleExecute(sqlString1, entityManager, routeIdString, estdate, serviceString);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		String uuidString2 = UUID.randomUUID().toString();
		String uuidString3 = UUID.randomUUID().toString();

		String myCompanyString = "";

		CompanyUserEntity companyUserEntity = companyUserRepository
				.findByUser(loginService.getLoginUser(request).getCommonInfo());

		myCompanyString = companyUserEntity.getCompany().getPkCompany() + "";

		String sqlString2 = "insert into supply_chain_portal.route_company values (?, ?, ?)";
		try {

			CommonUtils.simpleExecute(sqlString2, entityManager, uuidString2, routeIdString, myCompanyString);
			CommonUtils.simpleExecute(sqlString2, entityManager, uuidString3, routeIdString, companyString);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
