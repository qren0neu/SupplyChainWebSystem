package com.qiren.supplier.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pkorderitem")
    private String pkOrderItem;

    @ManyToOne
    @JoinColumn(name = "fkorder", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "fkitemprice", nullable = false)
    private ItemPrice itemPrice;

    @Column(name = "quantity")
    private int quantity;

    // Getters and setters for the class properties go here...
}