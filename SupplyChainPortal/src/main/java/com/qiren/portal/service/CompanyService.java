package com.qiren.portal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qiren.common.response.CommonResponse;
import com.qiren.common.tools.CommonUtils;
import com.qiren.portal.entities.CompanyEntity;
import com.qiren.portal.repository.CompanyRepo;
import com.qiren.portal.request.CompanyRegistrationRequest;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepo companyRepository;

	public CommonResponse findCompanyByType(String role) {
		List<CompanyEntity> companyEntities = new ArrayList<>();
		companyEntities.addAll(companyRepository.findByRole(role));
		return CommonUtils.success(companyEntities);
	}
	
	public String registerNewCompany(CompanyRegistrationRequest registrationRequest) {
		try {
			CompanyEntity companyEntity = new CompanyEntity();
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
			return e.getMessage();
		}
		return "";
	}
	
}
