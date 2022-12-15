package com.qiren.portal.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_company")
public class CompanyUserEntity {

    @Id
    @Column(name = "pkusercompany")
	@GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "fkuser")
    private CommonUserEntity user;

    @ManyToOne
    @JoinColumn(name = "fkcompany")
    private CompanyEntity company;
}

