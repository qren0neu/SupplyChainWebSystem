package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qiren.portal.entities.UserLoginEntity;

@Repository
public interface UserLoginEntityRepo extends JpaRepository<UserLoginEntity, String> {

	@Query(value = "select * from user_login where identifier = ?1 and credential = md5(?2)", nativeQuery = true)
	UserLoginEntity findByUsernameAndPassword(String username, String password);
	
	@Query(value = "select * from user_login where identifier = ?1", nativeQuery = true)
	UserLoginEntity findByUsername(String username);
}
