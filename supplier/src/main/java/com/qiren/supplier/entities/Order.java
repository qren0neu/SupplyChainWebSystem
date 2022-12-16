package com.qiren.supplier.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "pkorder")
    private String pkorder;

    @Column(name = "startdate", nullable = false)
    private Date startdate;

    @Column(name = "esttime", nullable = false)
    private int esttime;

    @Column(name = "fkseller", nullable = false)
    private long fkseller;

    @Column(name = "fkcustomer", nullable = false)
    private long fkcustomer;
}
