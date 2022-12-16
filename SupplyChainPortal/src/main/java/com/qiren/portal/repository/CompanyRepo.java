package com.qiren.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.portal.entities.CompanyEntity;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyEntity, Long> {

	List<CompanyEntity> findByRole(String role);

	CompanyEntity findByName(String name);
}
