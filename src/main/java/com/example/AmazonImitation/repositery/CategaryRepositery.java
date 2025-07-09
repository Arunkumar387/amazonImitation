package com.example.AmazonImitation.repositery;


import com.example.AmazonImitation.entity.Categary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategaryRepositery extends JpaRepository<Categary,Integer> {

}
