package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qiren.portal.entities.CommonUserEntity;
import com.qiren.portal.entities.CompanyEntity;
import com.qiren.portal.entities.CompanyUserEntity;

public interface CompanyUserRepository extends JpaRepository<CompanyUserEntity, String> {
	// define a method to find the company associated with a user
	CompanyUserEntity findByUser(CommonUserEntity user);
	
	@Query(value = "SELECT COUNT(*) FROM user_company uc1 JOIN user_company uc2 ON uc1.fkcompany = uc2.fkcompany WHERE uc1.fkuser = ?1 AND uc2.fkuser = ?2", nativeQuery = true)
	int countByUsers(String user1Id, String user2Id);
}
