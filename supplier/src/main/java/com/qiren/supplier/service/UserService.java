package com.qiren.supplier.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.supplier.entities.UserAuthEntity;
import com.qiren.supplier.repository.UserAuthRepo;
import com.qiren.supplier.request.UserAuthRequest;

@Service
public class UserService {

	@Autowired
	private UserAuthRepo userAuthRepo;
	
	public CommonResponse createAuth(UserAuthRequest userAuthRequest) {
		UserAuthEntity entity = new UserAuthEntity();
		entity.setCredential(userAuthRequest.getCredential());
		entity.setFkUser(Long.parseLong(userAuthRequest.getUserid()));
		entity.setIdentifier(userAuthRequest.getIdentifier());
		entity.setRole(userAuthRequest.getRole());
		entity.setType(userAuthRequest.getType());
		
		try {
			userAuthRepo.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
		
		return CommonUtils.success();
	}
	
}
