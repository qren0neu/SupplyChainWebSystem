package com.qiren.portal.service;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.UserLoginEntity;
import com.qiren.portal.repository.CommonUserRepository;
import com.qiren.portal.request.UserRegistrationRequest;

@Service
public class LoginService {
	
	@Autowired
	private CommonUserRepository userRepository;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public String registerUser(UserRegistrationRequest userRequest, String role) {
		try {
			CommonUserEntity entity = new CommonUserEntity();
			setUserData(userRequest, entity);
			entity.setRole(role);
			Object object = userRepository.save(entity);
		} catch (Exception e) {
			return "Internal Error: SQL Exception";
		}
		return "";
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
