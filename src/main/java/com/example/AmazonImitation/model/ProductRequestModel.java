package com.example.AmazonImitation.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestModel {
    public String productname;
    public String description;
    public int quantity;
    public int price;
    public int categaryid;
}
