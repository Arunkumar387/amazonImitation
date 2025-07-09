package com.example.AmazonImitation.model;

import com.example.AmazonImitation.entity.Categary;
import com.example.AmazonImitation.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategaryResponseModel {
    public int categaryid;
    public String categaryname;
    public List<Product> productList;
}
