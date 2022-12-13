package com.qiren.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.portal.entities.CommonUserEntity;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUserEntity, Long> {

}
