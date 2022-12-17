package com.qiren.manufacture.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "item")
public class Item {

	@Id
	@Column(name = "pkitem", nullable = false)
	private long pkitem;
	
	@Column(name = "itemname", nullable = false)
	private String itemname;
}
