package com.qiren.portal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "UserLogin")
@Data
public class UserLoginEntity {
	@Id
	@Column(name = "pkUserLogin")
	private String pkUserLogin;

	@Column(name = "fkUser")
	private int fkUser;

	@Column(name = "type")
	private String type;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "credential")
	private String credential;

	@Column(name = "role")
	private String role;
}
