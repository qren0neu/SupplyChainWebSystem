package com.qiren.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.UserAuthEntity;

@Repository
public interface UserAuthRepo extends JpaRepository<UserAuthEntity, String> {

//	@Query(value = "select * from user_auth where identifier = ?1 and credential = ?2")
	UserAuthEntity findByIdentifierAndCredential(String identifier, String credential);

	UserAuthEntity findByFkUser(String fkUser);
}
