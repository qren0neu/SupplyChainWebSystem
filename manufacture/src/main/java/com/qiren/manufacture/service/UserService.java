package com.qiren.manufacture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.response.CommonUserResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.manufacture.entities.UserAuthEntity;
import com.qiren.manufacture.repository.UserAuthRepo;
import com.qiren.manufacture.request.UserAuthRequest;

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
		entity.setFkcompany(userAuthRequest.getCompany());
		
		try {
			userAuthRepo.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
		
		return CommonUtils.success();
	}


	public CommonResponse getAuth(UserAuthRequest userAuthRequest) {
		try {
			UserAuthEntity entity = userAuthRepo.findByIdentifierAndCredential(userAuthRequest.getIdentifier(),
					userAuthRequest.getCredential());
			CommonUserResponse response = new CommonUserResponse();
			response.setRole(entity.getRole());
			response.setType(entity.getType());
			response.setUsername(entity.getIdentifier());
			return CommonUtils.success(response);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
	}
	
	public CommonResponse updateAuth(UserAuthRequest userAuthRequest) {
		
		try {
			UserAuthEntity entity = userAuthRepo.findByFkUser(userAuthRequest.getUserid());
			entity.setCredential(userAuthRequest.getCredential());
			entity.setFkUser(Long.parseLong(userAuthRequest.getUserid()));
			entity.setIdentifier(userAuthRequest.getIdentifier());
//			entity.setRole(userAuthRequest.getRole());
//			entity.setType(userAusshRequest.getType());
			
			userAuthRepo.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonUtils.fail(e.getMessage());
		}
		
		return CommonUtils.success();
	}
}
