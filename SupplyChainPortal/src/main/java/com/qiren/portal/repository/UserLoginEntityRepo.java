package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qiren.portal.entities.UserLoginEntity;

public interface UserLoginEntityRepo extends JpaRepository<UserLoginEntity, String> {

	@Query(value = "select * from user_login where identifier = ?1 and credential = md5(?2)", nativeQuery = true)
	UserLoginEntity findByUsernameAndPassword(String username, String password);
}
