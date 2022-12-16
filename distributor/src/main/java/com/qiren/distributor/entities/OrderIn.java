package com.qiren.distributor.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_in")
public class OrderIn {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pkorderin", nullable = false)
    private String pkorderin;

    @Column(name = "itemname", nullable = false, columnDefinition = "varchar(45) DEFAULT 'NU Phone Prototype'")
    private String itemname;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(45)")
    private String status;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "price")
    private int price;

    @Column(name = "startdate", nullable = false, columnDefinition = "varchar(45)")
    private Date startdate;

    @Column(name = "esttime", nullable = false, columnDefinition = "varchar(45)")
    private int esttime;

    @Column(name = "fkseller", nullable = false, columnDefinition = "varchar(45)")
    private long fkseller;

    @Column(name = "fkorder", nullable = false, columnDefinition = "varchar(45)")
    private String fkorder;

    @Column(name = "fkcustomer", nullable = false, columnDefinition = "varchar(45)")
    private long fkcustomer;
}
