package com.qiren.supplier.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pkitem")
	private long pkItem;

	@Column(name = "name")
	private String name;

	// getters and setters
}
