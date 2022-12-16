package com.qiren.distributor.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_in")
public class OrderIn {
    @Id
    @Column(name = "pkorderin", nullable = false)
    private String pkorderin;

    @Column(name = "itemname", nullable = false, columnDefinition = "varchar(45) DEFAULT 'NU Phone Prototype'")
    private String itemname;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(45)")
    private String status;

    @Column(name = "startdate", nullable = false, columnDefinition = "varchar(45)")
    private String startdate;

    @Column(name = "esttime", nullable = false, columnDefinition = "varchar(45)")
    private String esttime;

    @Column(name = "fkseller", nullable = false, columnDefinition = "varchar(45)")
    private String fkseller;

    @Column(name = "fkorder", nullable = false, columnDefinition = "varchar(45)")
    private String fkorder;

    @Column(name = "fkcustomer", nullable = false, columnDefinition = "varchar(45)")
    private String fkcustomer;
}
