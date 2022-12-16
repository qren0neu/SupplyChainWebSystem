package com.qiren.distributor.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item_price")
public class ItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pkitemprice", nullable = false)
    private String pkitemprice;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "fkcompany", nullable = false)
    private long fkcompany;
    
    @ManyToOne
    @JoinColumn(name = "fkitem")
    private Item fkitem;
}

