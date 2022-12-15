package com.qiren.portal.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.response.CommonUserResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.common.tools.Constants;
import com.qiren.common.tools.InternalRole;
import com.qiren.common.tools.Role;
import com.qiren.portal.beans.UserBean;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.UserLoginEntity;
import com.qiren.portal.repository.CommonUserRepository;
import com.qiren.portal.repository.UserLoginEntityRepo;
import com.qiren.portal.request.UserRegistrationRequest;

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

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public UserBean getLoginUser(String userId) {
		Object o = redisTemplate.opsForHash().get(Constants.SESSION_KEY, userId);
		if (o == null) {
			return null;
		}
		return (UserBean) o;
	}

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

	public String registerUser(UserRegistrationRequest userRequest, String role) {
		try {
			CommonUserEntity entity = new CommonUserEntity();
			setUserData(userRequest, entity);
			entity.setRole(role);
			CommonUserEntity object = userRepository.save(entity);
			// if user is customer, store onto UserLogin automatically
//			if (Role.valueOf(role.toUpperCase()) == Role.CUSTOMER) {
//				boolean createLoginRes = createLogin(object);
//				if (createLoginRes) {
//					return "";
//				} else {
//					return "Internal Error: Failed to create User";
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Internal Error: Failed to create User";
		}
		return "";
	}
	
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

	public CommonResponse createLoginUser(String username, String role) {
		CommonUserEntity entity = findUserInfoByName(username);

		if (null == entity) {
			return CommonUtils.fail("User not exist");
		}

		boolean createLoginRes = createLogin(entity);
		return createLoginRes ? CommonUtils.success() : CommonUtils.fail("");
	}

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

		HttpSession session = request.getSession();
//		session.setAttribute(Constants.SESSION_KEY, response.getUserid());
		session.setAttribute(Constants.SESSION_KEY, CommonUtils.md5(response.getUsername()));

		return CommonUtils.success(response);
	}

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
//		userResponse.setType(roleString);
//		userResponse.setUserid(userEntity.getPkUser());
		userResponse.setUsername(username);

		// store
		UserBean userBean = new UserBean();
		userBean.setFname(userEntity.getFname());
		userBean.setRole(role);
//		userBean.setType(userResponse.getType());
		userBean.setUserid(userResponse.getUserid());
		userBean.setUsername(username);

//		redisTemplate.opsForHash().put(Constants.SESSION_KEY, userBean.getUserid(), userBean);
		redisTemplate.opsForHash().put(Constants.SESSION_KEY, CommonUtils.md5(userBean.getUsername()), userBean);
		
		return userResponse;
	}

	public CommonUserResponse customerLogin(String username, String password, String fname) {
		// note: customer only!
		// customer login
		// we should probably also use usertable not logintable here, but I'm considering
		UserLoginEntity loginEntity = loginRepository.findByUsernameAndPassword(username, password);
		if (null != loginEntity) {
			CommonUserResponse userResponse = new CommonUserResponse();
			userResponse.setUserid(loginEntity.getPkUserLogin());
			userResponse.setUsername(username);
			userResponse.setRole(Role.CUSTOMER.toString());
			userResponse.setType(InternalRole.Customer.CUSTOMER);

			// store
			UserBean userBean = new UserBean();
			userBean.setFname(fname);
			userBean.setRole("customer");
			userBean.setType(userResponse.getType());
			userBean.setUserid(userResponse.getUserid());
			userBean.setUsername(username);

//			redisTemplate.opsForHash().put(Constants.SESSION_KEY, userBean.getUserid(), userBean);
			redisTemplate.opsForHash().put(Constants.SESSION_KEY, CommonUtils.md5(userBean.getUsername()), userBean);

			return userResponse;
		} else {
			return null;
		}
	}

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
	
	public boolean checkLoginCorrect(String username) {
		UserBean userBean = getLoginUser(username);
		if (null == userBean) {
			return false;
		}
		return userBean.getUsername().equals(username);
	}
	
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

	public boolean createLogin(CommonUserEntity userEntity, String type) {
		// customer only
		UserLoginEntity entity = new UserLoginEntity();
		entity.setCredential(userEntity.getPassword());
		entity.setFkUser(userEntity.getPkUser());
		entity.setIdentifier(userEntity.getUsername());
		entity.setRole(userEntity.getRole());
		entity.setType(type);
		loginRepository.save(entity);
		return true;
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
