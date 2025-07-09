package com.example.AmazonImitation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", initialValue = 1, allocationSize = 1)
    public int productid;

    public String productname;
    public String description;
    public int quantity;
    public int price;

    @ManyToOne
    @JoinColumn(name = "categaryid")
    @JsonBackReference
    public Categary categary;
}
