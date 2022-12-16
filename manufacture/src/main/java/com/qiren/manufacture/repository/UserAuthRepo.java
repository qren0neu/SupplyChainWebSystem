package com.qiren.manufacture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.manufacture.entities.UserAuthEntity;


@Repository
public interface UserAuthRepo extends JpaRepository<UserAuthEntity, Long>{

//	@Query(value = "select * from user_auth where identifier = ?1 and credential = ?2")
	UserAuthEntity findByIdentifierAndCredential(String identifier, String credential);
}