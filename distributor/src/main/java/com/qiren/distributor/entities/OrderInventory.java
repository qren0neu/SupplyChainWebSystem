package com.qiren.distributor.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class OrderInventory {
	@Id
	private long fkorderin;
	private long total;
	private long instock;
	private long remain;
	private String name;
	private long company;

	// getters and setters for each field
}