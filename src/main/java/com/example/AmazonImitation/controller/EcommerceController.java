package com.example.AmazonImitation.controller;

import com.example.AmazonImitation.entity.Categary;
import com.example.AmazonImitation.entity.Product;
import com.example.AmazonImitation.model.CategaryRequestModel;
import com.example.AmazonImitation.model.CategaryResponseModel;
import com.example.AmazonImitation.model.ProductRequestModel;
import com.example.AmazonImitation.model.ProductResponseModel;
import com.example.AmazonImitation.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class EcommerceController {

    @Autowired
    ProductServices productServices;

    @PostMapping("/savecategary")
    public CategaryResponseModel save(@RequestBody CategaryRequestModel categaryRequestModel){
        return productServices.toentity(categaryRequestModel);
    }

    @PostMapping("/saveproduct")
    public ProductResponseModel save(@RequestBody ProductRequestModel productRequestModel){

        return productServices.toEntity(productRequestModel);
    }

    @GetMapping("/getCategary")
    public Optional<Categary> getCategory(int Categaryid){
        return productServices.getthecategary(Categaryid);
    }

    @GetMapping("/getProduct")
    public Optional<Product> getProduct(int productid){
        return productServices.gettheproduct(productid);
    }

    @DeleteMapping("/deleteCategary")
    public void deleteCategary(int categoryId){
        productServices.deletecategary(categoryId);
    }

    @DeleteMapping("/deleteProduct")
    public void deleteProduct(int productid){
        productServices.deleteproduct(productid);
    }


}
