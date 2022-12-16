package com.qiren.supplier.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item_price")
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pkitemprice")
    private long pkItemPrice;

    @ManyToOne
    @JoinColumn(name = "fkitem")
    private Item fkItem;

    @Column(name = "fkcompany")
    private long fkCompany;

    @Column(name = "price")
    private float price;

    @Column(name = "unit")
    private int unit;

    @Column(name = "instock")
    private int inStock;

    @Column(name = "status")
    private int status;

    // getters and setters
}
