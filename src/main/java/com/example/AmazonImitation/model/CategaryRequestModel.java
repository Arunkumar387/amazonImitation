package com.example.AmazonImitation.model;

import com.example.AmazonImitation.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategaryRequestModel {

    public String categaryname;
    public List<Product> productList;
}
