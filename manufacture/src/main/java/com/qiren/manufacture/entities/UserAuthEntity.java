package com.qiren.manufacture.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "UserAuth")
@Data
public class UserAuthEntity {
	@Id
	@Column(name = "pkuserauth")
	@GeneratedValue(strategy = GenerationType.UUID)
	private String pkUserLogin;

	@Column(name = "fkuser")
	private long fkUser;

	@Column(name = "type")
	private String type;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "credential")
	private String credential;

	@Column(name = "role")
	private String role;
	
	@Column(name = "fkcompany")
	private int fkcompany;
}
