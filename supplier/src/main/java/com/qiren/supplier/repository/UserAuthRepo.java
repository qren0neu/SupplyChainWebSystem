package com.qiren.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.UserAuthEntity;

@Repository
public interface UserAuthRepo extends JpaRepository<UserAuthEntity, Long>{

}
