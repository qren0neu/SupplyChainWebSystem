package com.qiren.supplier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qiren.supplier.entities.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>{

}
