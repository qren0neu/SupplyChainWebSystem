package com.qiren.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.InternalRole;
import com.qiren.common.tools.RestManager;
import com.qiren.common.tools.Role;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.CompanyEntity;
import com.qiren.portal.entities.CompanyUserEntity;
import com.qiren.portal.repository.CommonUserRepository;
import com.qiren.portal.repository.CompanyRepo;
import com.qiren.portal.repository.CompanyUserRepository;
import com.qiren.portal.request.CompanyRegistrationRequest;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepo companyRepository;
	@Autowired
	private CompanyUserRepository companyUserRepository;
	@Autowired
	private CommonUserRepository userRepository;
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Get all company by "supplier", "distributor", etc
	 */
	public CommonResponse findCompanyByType(String role) {
		List<CompanyEntity> companyEntities = new ArrayList<>();
		companyEntities.addAll(companyRepository.findByRole(role));
		return CommonUtils.success(companyEntities);
	}

	public CommonResponse findAllCompany() {
		List<CompanyEntity> companyEntities = new ArrayList<>();
		companyEntities.addAll(companyRepository.findAll());
		return CommonUtils.success(companyEntities);
	}

	/**
	 * 1. Create new company
	 * 2. Change user role out of "common"
	 * 3. Auth login info to remote server
	 * 4. Relate user with company
	 */
	public CommonResponse registerCompanyAndModifyUser(LoginService loginService,
			CompanyRegistrationRequest companyRegisRequest, String username) {
		
		CommonUserEntity entity = loginService.findFullUserInfoByName(username);

		if (null == entity) {
			return CommonUtils.fail("username not found");
		}

		Role role = Role.valueOf(entity.getRole().toUpperCase());

		if (role != Role.COMMON) {
			return CommonUtils.fail("invalid role");
		}

		// Step 1: create the company
		CompanyEntity companyEntity = registerNewCompany(companyRegisRequest);
		if (null == companyEntity) {
			return CommonUtils.fail("Create company failed");
		}
		// Step 2: change user type
		String companyRole = companyRegisRequest.getRole();
		Role enumCompanyRole = Role.valueOf(companyRole.toUpperCase());

		String userRoleNew = "";
		entity.setRole(companyRole);
		try {
			userRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
		
		boolean resp = false;
		// Step 3: save the user to remote project
		CommonResponse remoteResponse = null;
		
		HashMap<String, Object> requestMap = new HashMap<>();
		requestMap.put("userid", entity.getPkUser());
		requestMap.put("credential", entity.getPassword());
		requestMap.put("identifier", entity.getUsername());
		requestMap.put("role", entity.getRole());
		
		String url = "";
		
		switch (enumCompanyRole) {
		case SUPPLIER: {
			userRoleNew = InternalRole.Supplier.COMPANY_MANAGER;
			url = Constants.URL_SUPPLIER;
			break;
		}
		case DISTRIBUTOR: {
			userRoleNew = InternalRole.Distributor.COMPANY_MANAGER;
			url = Constants.URL_DISTRIBUTOR;
			break;
		}
		case MANUFACTURER: {
			url = Constants.URL_MANUFACTURER;
			userRoleNew = InternalRole.Manufacturer.COMPANY_MANAGER;
			break;
		}
		case ROUTER: {
			url = Constants.URL_ROUTER;
			userRoleNew = InternalRole.Router.ROUTE_PLANNER;
			break;
		}
		default:
			return CommonUtils.fail("Invalid input");
		}

		requestMap.put("type", userRoleNew);
		url += "/api/user/createAuth";
		
		remoteResponse = RestManager.getInstance().sendHttpPost(restTemplate, url, requestMap);
		resp = remoteResponse.getStatusCode() == 0;
		
		// boolean resp = loginService.createLogin(entity, userRoleNew);
		
		CompanyUserEntity companyUserEntity = new CompanyUserEntity();
		companyUserEntity.setCompany(companyEntity);
		companyUserEntity.setUser(entity);
		// Step 4: relate the manager with company
		try {
			companyUserRepository.save(companyUserEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}

		if (!resp) {
			return CommonUtils.fail("Create new login failed");
		}

		return CommonUtils.frontEndRedirect("/login/userLogin");
	}

	/**
	 * Register a new company
	 */
	public CompanyEntity registerNewCompany(CompanyRegistrationRequest registrationRequest) {
		CompanyEntity companyEntity = new CompanyEntity();
		try {
			companyEntity.setName(registrationRequest.getName());
			companyEntity.setAddress(registrationRequest.getAddress());
			companyEntity.setPhone(registrationRequest.getPhone());
			companyEntity.setEmail(registrationRequest.getEmail());
			companyEntity.setRole(registrationRequest.getRole());
			companyEntity.setCity(registrationRequest.getCity());
			companyEntity.setState(registrationRequest.getState());
			companyEntity.setCountry(registrationRequest.getCountry());

			companyRepository.save(companyEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return companyEntity;
	}

}
