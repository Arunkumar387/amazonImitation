package com.example.AmazonImitation.repositery;


import com.example.AmazonImitation.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositery extends JpaRepository<Product,Integer> {
}
