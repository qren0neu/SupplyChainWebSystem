package com.qiren.portal.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.qiren.common.response.CommonResponse;
import com.qiren.common.response.CommonUserResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.InternalRole;
import com.qiren.common.tools.RestManager;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.UserLoginEntity;
import com.qiren.portal.repository.CommonUserRepository;
import com.qiren.portal.repository.UserLoginEntityRepo;
import com.qiren.portal.request.UserRegistrationRequest;
import com.qiren.portal.request.UserUpdateLoginRequest;
import com.qiren.portal.request.UserUpdateRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {

	@Autowired
	private CommonUserRepository userRepository;
	@Autowired
	private UserLoginEntityRepo loginRepository;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RestTemplate restTemplate;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public boolean isLogin(HttpServletRequest request) {
		return null != getLoginUser(request);
	}

	/**
	 * Get login user info
	 */
	public UserBean getLoginUser(String userId) {
		Object o = redisTemplate.opsForHash().get(Constants.SESSION_KEY, userId);
		if (o == null) {
			return null;
		}
		return (UserBean) o;
	}

	/**
	 * Get login user info
	 */
	public UserBean getLoginUser(HttpServletRequest request) {
		Object sessionObject = request.getSession().getAttribute(Constants.SESSION_KEY);

		if (sessionObject == null) {
			return null;
		}

		String userId = sessionObject.toString();

		Object o = redisTemplate.opsForHash().get(Constants.SESSION_KEY, userId);
		if (o == null) {
			return null;
		}
		return (UserBean) o;
	}

	public void clearLoginUser(String userId) {
		redisTemplate.opsForHash().delete(Constants.SESSION_KEY, userId);
	}

	/**
	 * Register as a common user
	 */
	public String registerUser(UserRegistrationRequest userRequest, String role) {
		try {
			CommonUserEntity entity = new CommonUserEntity();
			setUserData(userRequest, entity);
			entity.setRole(role);
			CommonUserEntity object = userRepository.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return "Create user failed, please check your info or change your username";
		}
		return "";
	}

	/**
	 * Store customer into login table (not common user table)
	 */
	public CommonResponse customerRegisterLogin(String username, String role) {
		// update common user
		CommonUserEntity entity = findFullUserInfoByName(username);

		if (null == entity || Role.valueOf(entity.getRole().toUpperCase()) != Role.COMMON) {
			// only change common user here!
			return CommonUtils.fail("Invalid Request");
		}

		entity.setRole(role);
		userRepository.save(entity);

		// register to login table
		boolean createLoginRes = createLogin(entity);
		return createLoginRes ? CommonUtils.success() : CommonUtils.fail("");
	}

	/**
	 * Create user into login table (not common user table)
	 */
	public CommonResponse createLoginUser(String username, String role) {
		CommonUserEntity entity = findUserInfoByName(username);

		if (null == entity) {
			return CommonUtils.fail("User not exist");
		}

		boolean createLoginRes = createLogin(entity);
		return createLoginRes ? CommonUtils.success() : CommonUtils.fail("");
	}

	/**
	 * Login
	 */
	public CommonResponse login(String username, String password, HttpServletRequest request) {
		CommonUserEntity userEntity = findUserInfoByName(CommonUtils.md5(username));

		if (null == userEntity) {
			return CommonUtils.fail("Username not exist");
		}

		String role = userEntity.getRole();

		CommonUserResponse response = null;
		if (Role.valueOf(role.toUpperCase()) == Role.CUSTOMER) {
			response = customerLogin(username, password, userEntity.getFname());
		} else if (Role.valueOf(role.toUpperCase()) == Role.COMMON) {
			response = new CommonUserResponse();
			response.setRole(role);
			response.setUsername(username);
			// don't create session
			return CommonUtils.success(response);
		} else {
			response = otherLogin(username, password, role);
		}

		if (null == response) {
			return CommonUtils.fail("Wrong username or password");
		}

		response.setFname(userEntity.getFname());

		CommonUserResponse remoteResponse = getUserInfoFromSubSystem(username, password, role);
		if (null == remoteResponse) {
			return CommonUtils.fail("Sub systems authorization failed");
		}

		response.setType(remoteResponse.getType());

		UserBean userBean = new UserBean();

		userBean.setFname(userEntity.getFname());
		userBean.setType(response.getType());
		userBean.setRole(response.getRole());
		userBean.setUsername(remoteResponse.getUsername());
		userBean.setUserid(userEntity.getPkUser() + "");
		// put as many as possible info here, redis is powerful
		userBean.setCommonInfo(userEntity);

		redisTemplate.opsForHash().put(Constants.SESSION_KEY, CommonUtils.md5(response.getUsername()), userBean);

		HttpSession session = request.getSession();
		// session.setAttribute(Constants.SESSION_KEY, response.getUserid());
		session.setAttribute(Constants.SESSION_KEY, CommonUtils.md5(response.getUsername()));

		return CommonUtils.success(response);
	}

	/**
	 * Login for users out of "customer"
	 */
	public CommonUserResponse otherLogin(String username, String password, String role) {
		// I checked again and think that we should use this portal for login
		// other portal just authenticate

		CommonUserEntity userEntity = userRepository.findByUsernameAndPassword(username, password);
		if (null == userEntity) {
			return null;
		}
		String roleString = userEntity.getRole();

		CommonUserResponse userResponse = new CommonUserResponse();

		userResponse.setRole(roleString);
		userResponse.setUsername(username);

		// store
		UserBean userBean = new UserBean();
		userBean.setFname(userEntity.getFname());
		userBean.setRole(role);
		// userBean.setUserid(userResponse.getUserid());
		userBean.setUsername(username);

		// redisTemplate.opsForHash().put(Constants.SESSION_KEY,
		// CommonUtils.md5(userBean.getUsername()), userBean);

		return userResponse;
	}

	/**
	 * Login for "customer"
	 */
	public CommonUserResponse customerLogin(String username, String password, String fname) {
		// note: customer only!
		// customer login
		// we should probably also use usertable not logintable here, but I'm
		// considering
		UserLoginEntity loginEntity = loginRepository.findByUsernameAndPassword(username, password);
		if (null != loginEntity) {
			CommonUserResponse userResponse = new CommonUserResponse();
			// userResponse.setUserid(loginEntity.getPkUserLogin());
			userResponse.setUsername(username);
			userResponse.setRole(Role.CUSTOMER.toString().toLowerCase());
			userResponse.setType(InternalRole.Customer.CUSTOMER);

			// store
			// UserBean userBean = new UserBean();
			// userBean.setFname(fname);
			// userBean.setRole("customer");
			// userBean.setType(userResponse.getType());
			// // userBean.setUserid(userResponse.getUserid());
			// userBean.setUsername(username);

			// redisTemplate.opsForHash().put(Constants.SESSION_KEY,
			// CommonUtils.md5(userBean.getUsername()), userBean);

			return userResponse;
		} else {
			return null;
		}
	}

	/**
	 * Get all user info (with password) note that here the username is md5
	 */
	public CommonUserEntity findFullUserInfoByName(String userName) {
		// note that here the username is md5
		try {
			CommonUserEntity users = userRepository.findByUsername(userName);
			if (null == users) {
				return null;
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Check the username(md5) here is the same as login user
	 */
	public boolean checkLoginCorrect(String username) {
		UserBean userBean = getLoginUser(username);
		if (null == userBean) {
			return false;
		}
		return userBean.getUsername().equals(username);
	}

	/**
	 * Get all user info (without password) note that here the username is md5
	 */
	public CommonUserEntity findUserInfoByName(String userName) {
		// safe method to avoid returning password
		// note that here the username is md5
		CommonUserEntity users = findFullUserInfoByName(userName);
		if (null == users) {
			return null;
		}
		users.setPassword(null);
		return users;
	}

	/**
	 * Save user into login table (not common table) Customer Only
	 */
	public boolean createLogin(CommonUserEntity userEntity) {
		// customer only
		UserLoginEntity entity = new UserLoginEntity();
		entity.setCredential(userEntity.getPassword());
		entity.setFkUser(userEntity.getPkUser());
		entity.setIdentifier(userEntity.getUsername());
		entity.setRole(userEntity.getRole());
		entity.setType(InternalRole.Customer.CUSTOMER);
		loginRepository.save(entity);
		return true;
	}

	@Deprecated
	public boolean createLogin(CommonUserEntity userEntity, String type) {
		UserLoginEntity entity = new UserLoginEntity();
		entity.setCredential(userEntity.getPassword());
		entity.setFkUser(userEntity.getPkUser());
		entity.setIdentifier(userEntity.getUsername());
		entity.setRole(userEntity.getRole());
		entity.setType(type);
		loginRepository.save(entity);
		return true;
	}

	public CommonUserResponse getUserInfoFromSubSystem(String username, String password, String role) {
		// password we need already hashed
		HashMap<String, Object> requestMap = new HashMap<>();
		// requestMap.put("userid", entity.getPkUser());
		requestMap.put("credential", CommonUtils.md5(password));
		requestMap.put("identifier", username);
		requestMap.put("role", role);

		String url = "";

		Role enumCompanyRole = Role.valueOf(role.toUpperCase());
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
			UserLoginEntity userLoginEntity = loginRepository.findByUsernameAndPassword(username, password);
			CommonUserResponse commonUserResponse = new CommonUserResponse();
			commonUserResponse.setRole(userLoginEntity.getRole());
			commonUserResponse.setType(userLoginEntity.getType());
			commonUserResponse.setUsername(userLoginEntity.getIdentifier());
			return commonUserResponse;
		}

		url += "/api/user/getAuth";

		CommonResponse remoteResponse = RestManager.getInstance().sendHttpPost(restTemplate, url, requestMap);
		boolean success = remoteResponse.getStatusCode() == 0;

		if (!success) {
			return null;
		}

		String body = remoteResponse.getData();
		CommonUserResponse userResponse = new Gson().fromJson(body, CommonUserResponse.class);
		return userResponse;
	}

	/**
	 * Update user info without username and password
	 */
	public CommonResponse updateCommonInfo(HttpServletRequest servletRequest, UserUpdateRequest request) {
		UserBean userBean = getLoginUser(servletRequest);

		if (null == userBean) {
			return CommonUtils.fail("Not logged in");
		}

		// CommonUserEntity user = userBean.getCommonInfo();
		CommonUserEntity user = findFullUserInfoByName(CommonUtils.md5(userBean.getUsername()));

		// do not change 1.pk, 2.username, 3.password
		user.setFname(request.getFname());
		user.setMname(request.getMname());
		user.setLname(request.getLname());
		user.setGender(request.getGender());
		user.setDob(simpleDateFormat.format(request.getBirthday()));
		user.setAddress1(request.getAddress1());
		user.setAddress2(request.getAddress2());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPref(request.getPreference());

		try {
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Update user failed");
		}

		userBean.setCommonInfo(user);

		// update session info
		redisTemplate.opsForHash().put(Constants.SESSION_KEY, CommonUtils.md5(user.getUsername()), userBean);
		return CommonUtils.success();
	}

	/**
	 * Update user username and password
	 */
	public CommonResponse updateLoginInfo(HttpServletRequest servletRequest, UserUpdateLoginRequest loginRequest) {
		UserBean userBean = getLoginUser(servletRequest);

		if (null == userBean) {
			return CommonUtils.fail("Not logged in");
		}
		
		CommonUserEntity user = userBean.getCommonInfo();
		String oriPasswordString = user.getPassword();
		user.setUsername(loginRequest.getUsername());
		user.setPassword(CommonUtils.md5(loginRequest.getPassword()));

		try {
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail("Update user failed");
		}

		userBean.setUsername(loginRequest.getUsername());

		// update remote

		CommonResponse remoteResponse = null;
		String companyRole = userBean.getRole();
		Role enumCompanyRole = Role.valueOf(companyRole.toUpperCase());

		HashMap<String, Object> requestMap = new HashMap<>();
		requestMap.put("userid", user.getPkUser());
		requestMap.put("credential", user.getPassword());
		requestMap.put("identifier", user.getUsername());

		String url = "";

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
		case CUSTOMER: {
			UserLoginEntity userLoginEntity = loginRepository.findByUsername(user.getUsername());
			userLoginEntity.setIdentifier(loginRequest.getUsername());
			userLoginEntity.setCredential(loginRequest.getPassword());
			loginRepository.save(userLoginEntity);

			clearLoginUser(CommonUtils.md5(userBean.getUsername()));
			servletRequest.getSession().setAttribute(Constants.SESSION_KEY, "");
			servletRequest.getSession().invalidate();
			return CommonUtils.success();
		}
		default:
			return CommonUtils.fail("Invalid input");
		}
		url += "/api/user/updateAuth";

		remoteResponse = RestManager.getInstance().sendHttpPost(restTemplate, url, requestMap);
		boolean resp = remoteResponse.getStatusCode() == 0;

		if (!resp) {
			return CommonUtils.fail("Sub System Failed");
		}

		clearLoginUser(CommonUtils.md5(userBean.getUsername()));
		servletRequest.getSession().setAttribute(Constants.SESSION_KEY, "");
		servletRequest.getSession().invalidate();

		return CommonUtils.success();
	}

	private void setUserData(UserRegistrationRequest request, CommonUserEntity user) {
		user.setPkUser(0);
		user.setFname(request.getFname());
		user.setMname(request.getMname());
		user.setLname(request.getLname());
		user.setGender(request.getGender());
		user.setDob(simpleDateFormat.format(request.getBirthday()));
		user.setAddress1(request.getAddress1());
		user.setAddress2(request.getAddress2());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setPref(request.getPreference());
		user.setUsername(request.getUsername());
		user.setPassword(CommonUtils.md5(request.getPassword()));
	}

}
