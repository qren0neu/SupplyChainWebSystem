package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qiren.portal.entities.CommonUserEntity;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUserEntity, Long> {

	@Query(value = "select * from common_user c where md5(username) = ?1", nativeQuery = true)
	CommonUserEntity findByUsername(String username);

	@Query(value = "select * from common_user c where username = ?1 and password = md5(?2)", nativeQuery = true)
	CommonUserEntity findByUsernameAndPassword(String username, String password);

}
