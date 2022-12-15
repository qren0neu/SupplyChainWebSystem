package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.CompanyUserEntity;

public interface CompanyUserRepository extends JpaRepository<CompanyUserEntity, String> {
	// define a method to find the company associated with a user
	CompanyUserEntity findByUser(CommonUserEntity user);
}
