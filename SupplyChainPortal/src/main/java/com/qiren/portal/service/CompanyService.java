package com.qiren.portal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.response.CommonUserResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.InternalRole;
import com.qiren.common.tools.RestManager;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.CompanyEntity;
import com.qiren.portal.entities.CompanyUserEntity;
import com.qiren.portal.repository.CommonUserRepository;
import com.qiren.portal.repository.CompanyRepo;
import com.qiren.portal.repository.CompanyUserRepository;
import com.qiren.portal.request.ChooseCompanyRequset;
import com.qiren.portal.request.CompanyRegistrationRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;

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
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private LoginService loginService;

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

	public CommonResponse viewSelfCompany(HttpServletRequest request, LoginService loginService) {
		UserBean userBean = loginService.getLoginUser(request);

		if (null == userBean) {
			return CommonUtils.fail("User not found");
		}

		CommonUserEntity commonUserEntity = loginService.findUserInfoByName(userBean.getUsername());

		CompanyUserEntity companyUserEntity = companyUserRepository.findByUser(commonUserEntity);
		// companyUserEntity.setUser(null);

		return CommonUtils.success(companyUserEntity.getCompany());
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
		requestMap.put("company", companyEntity.getPkCompany());

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

	public CommonResponse chooseCompany(LoginService loginService,
			ChooseCompanyRequset chooseCompanyRequset) {
		String usernameString = chooseCompanyRequset.getUsername();
		CommonUserEntity entity = loginService.findFullUserInfoByName(usernameString);

		if (null == entity) {
			return CommonUtils.fail("username not found");
		}

		Role role = Role.valueOf(entity.getRole().toUpperCase());

		if (role != Role.COMMON) {
			return CommonUtils.fail("invalid role");
		}

		String companyRole = chooseCompanyRequset.getCompanyRole();
		// update user role

		entity.setRole(companyRole);
		try {
			userRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}

		boolean resp = false;
		// save the user to remote project
		CommonResponse remoteResponse = null;

		HashMap<String, Object> requestMap = new HashMap<>();
		requestMap.put("userid", entity.getPkUser());
		requestMap.put("credential", entity.getPassword());
		requestMap.put("identifier", entity.getUsername());
		requestMap.put("role", entity.getRole());
		requestMap.put("type", chooseCompanyRequset.getPositionRole());
		requestMap.put("company", Long.parseLong(chooseCompanyRequset.getCompanyId()));

		String url = "";

		Role enumCompanyRole = Role.valueOf(companyRole.toUpperCase());

		switch (enumCompanyRole) {
			case SUPPLIER: {
				url = Constants.URL_SUPPLIER;
				break;
			}
			case DISTRIBUTOR: {
				url = Constants.URL_DISTRIBUTOR;
				break;
			}
			case MANUFACTURER: {
				url = Constants.URL_MANUFACTURER;
				break;
			}
			case ROUTER: {
				url = Constants.URL_ROUTER;
				break;
			}
			default:
				return CommonUtils.fail("Invalid input");
		}

		requestMap.put("type", chooseCompanyRequset.getPositionRole());
		url += "/api/user/createAuth";

		remoteResponse = RestManager.getInstance().sendHttpPost(restTemplate, url, requestMap);
		resp = remoteResponse.getStatusCode() == 0;

		CompanyEntity companyEntity = new CompanyEntity();
		companyEntity.setPkCompany(Long.parseLong(chooseCompanyRequset.getCompanyId()));

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

			companyEntity = companyRepository.findByName(companyEntity.getName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return companyEntity;
	}

	public boolean isInSameCompany(String user1, String user2) {

		return companyUserRepository.countByUsers(user1, user2) != 0;
	}
	
	public List<Object[]> getAllStaff(HttpServletRequest request, String companyId) {
		
		UserBean userBean = loginService.getLoginUser(request);
		long userIdString = userBean.getCommonInfo().getPkUser();
		
		String sqlString = "select username, concat(fname, ' ', mname, ' ', lname) 'fullname', gender from supply_chain_portal.user_company uc join supply_chain_portal.common_user cu on uc.fkuser = cu.pkuser join supply_chain_portal.company cm on uc.fkcompany = cm.pkcompany where uc.fkcompany = ? and exists (select fkUser from supply_chain_portal.user_company where fkCompany = ? and fkuser = ?);";
		
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, companyId);
		query.setParameter(2, companyId);
		query.setParameter(3, userIdString);
		
		return query.getResultList();
	}
	
	public List<Object[]> getAllStaff(HttpServletRequest request) {
		
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null == userBean) {
			return null;
		}
		
		long userIdString = userBean.getCommonInfo().getPkUser();
		
		CompanyUserEntity companyUserEntity = companyUserRepository.findByUser(userBean.getCommonInfo());
		
		String sqlString = "select username, concat(fname, ' ', mname, ' ', lname) 'fullname', gender from supply_chain_portal.user_company uc join supply_chain_portal.common_user cu on uc.fkuser = cu.pkuser join supply_chain_portal.company cm on uc.fkcompany = cm.pkcompany where uc.fkcompany = ? and exists (select fkUser from supply_chain_portal.user_company where fkCompany = ? and fkuser = ?);";
		
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter(1, companyUserEntity.getCompany().getPkCompany());
		query.setParameter(2, companyUserEntity.getCompany().getPkCompany());
		query.setParameter(3, userIdString);
		
		return query.getResultList();
	}
	
	public boolean isManager(HttpServletRequest request) {
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null == userBean) {
			return false;
		}
		
		return InternalRole.Distributor.COMPANY_MANAGER.equals(userBean.getType())
				|| InternalRole.Supplier.COMPANY_MANAGER.equals(userBean.getType())
				|| InternalRole.Manufacturer.COMPANY_MANAGER.equals(userBean.getType());
	}

	public CommonUserEntity getCompanyUserInfo(HttpServletRequest request, 
			String username) {
		CommonUserEntity entity = loginService.findFullUserInfoByName(username);
		
		long uid = entity.getPkUser();
		
		UserBean userBean = loginService.getLoginUser(request);
		
		if (null == userBean) {
			return null;
		}
		
		long myid = userBean.getCommonInfo().getPkUser();
		
		if (!isInSameCompany(myid + "", uid + "")) {
			return null;
		}
		
		return entity;
	}
}