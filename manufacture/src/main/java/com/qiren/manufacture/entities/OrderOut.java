package com.qiren.manufacture.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_out")
public class OrderOut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pkorderout", nullable = false)
    private String pkorderout;

    @Column(name = "quantity", nullable = false, columnDefinition = "varchar(45)")
    private String quantity;

    @Column(name = "startdate", nullable = false, columnDefinition = "date")
    private Date startdate;

    @Column(name = "esttime", nullable = false)
    private int esttime;

    @Column(name = "fkcustomer", nullable = false, columnDefinition = "varchar(45)")
    private long fkcustomer;

    @ManyToOne
    @JoinColumn(name = "fkorderin", nullable = false, columnDefinition = "varchar(45)")
    private OrderIn fkorderin;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(45)")
    private String status;

    @ManyToOne
    @JoinColumn(name = "fkitemprice", nullable = false, columnDefinition = "varchar(45)")
    private ItemPrice fkitemprice;
}
