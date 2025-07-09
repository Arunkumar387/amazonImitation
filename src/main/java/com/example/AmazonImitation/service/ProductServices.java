package com.example.AmazonImitation.service;


import com.example.AmazonImitation.entity.Categary;
import com.example.AmazonImitation.entity.Product;
import com.example.AmazonImitation.model.CategaryRequestModel;
import com.example.AmazonImitation.model.CategaryResponseModel;
import com.example.AmazonImitation.model.ProductRequestModel;
import com.example.AmazonImitation.model.ProductResponseModel;
import com.example.AmazonImitation.repositery.CategaryRepositery;
import com.example.AmazonImitation.repositery.ProductRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServices  {

    @Autowired
    CategaryRepositery categaryRepositery;

    @Autowired
    ProductRepositery productRepositery;

    public CategaryResponseModel toentity(CategaryRequestModel categaryRequestModel) {
        if (categaryRequestModel == null) {
            return null;
        } else {
            Categary categary = new Categary();
            categary.setCategaryname(categaryRequestModel.getCategaryname());
            return tomodel(categaryRepositery.save(categary));
        }
    }

    public CategaryResponseModel tomodel(Categary categary) {
        if (categary == null) {
            return null;
        } else {
            CategaryResponseModel categaryResponseModel = new CategaryResponseModel();
            categaryResponseModel.setCategaryid(categary.getCategaryid());
            categaryResponseModel.setCategaryname(categary.getCategaryname());
//            List<Product> list = productRepositery.findAll();
//            List<Product> productList = list.stream().filter(product -> product.getCategaryid().equals(categary.getCategaryid())).collect(Collectors.toList());
//            categaryResponseModel.setProductList(productList);
            return categaryResponseModel;
        }
    }

    public ProductResponseModel toEntity(ProductRequestModel productRequestModel) {
        if (productRequestModel == null) {
            return null;
        } else {
            Product product = new Product();
            product.setProductname(productRequestModel.getProductname());
            product.setDescription(productRequestModel.getDescription());
            product.setQuantity(productRequestModel.getQuantity());
            product.setPrice(productRequestModel.getPrice());
            product.setCategary(categaryRepositery.getReferenceById(productRequestModel.getCategaryid()));
            return Tomodel(productRepositery.save(product));
        }
    }


    public ProductResponseModel Tomodel(Product product) {
        if (product == null) {
            return null;
        } else {
            ProductResponseModel productResponseModel = new ProductResponseModel();
            productResponseModel.setProductid(product.getProductid());
            productResponseModel.setProductname(product.getProductname());
            productResponseModel.setDescription(product.getDescription());
            productResponseModel.setQuantity(product.getQuantity());
            productResponseModel.setPrice(product.getPrice());
            productResponseModel.setCategaryid(product.getCategary().getCategaryid());
            return productResponseModel;
        }
    }

    public Optional<Categary> getthecategary(int id) {
        if (categaryRepositery.existsById(id))
        {
           Optional<Categary> categary=categaryRepositery.findById(id);
         return categary;
        }
        else {
            return null;
        }
      }

    public Optional<Product> gettheproduct(int id) {
        if (productRepositery.existsById(id))
        {
            Optional<Product> product=productRepositery.findById(id);
            return product;
        }
        else {
            return null;
        }

    }

    public void deletecategary(int id) {
        categaryRepositery.deleteById(id);
    }

    public void deleteproduct(int id) {
        productRepositery.deleteById(id);
    }


}
