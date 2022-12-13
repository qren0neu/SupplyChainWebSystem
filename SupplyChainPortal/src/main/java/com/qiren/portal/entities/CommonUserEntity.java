package com.qiren.portal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CommonUser")
public class CommonUserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "pkuser")
  private long pkUser;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "dob", nullable = false)
  private String dob;

  @Column(name = "address1", nullable = false)
  private String address1;

  @Column(name = "address2")
  private String address2;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "pref", nullable = false)
  private String pref;

  @Column(name = "role", nullable = false)
  private String role;

  @Column(name = "fname", nullable = false)
  private String fname;

  @Column(name = "mname")
  private String mname;

  @Column(name = "lname", nullable = false)
  private String lname;

  @Column(name = "gender", nullable = false)
  private String gender;
}

