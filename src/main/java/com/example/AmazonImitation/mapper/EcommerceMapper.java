package com.example.AmazonImitation.mapper;

import com.example.AmazonImitation.entity.Categary;
import com.example.AmazonImitation.entity.Product;
import com.example.AmazonImitation.entity.User;
import com.example.AmazonImitation.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EcommerceMapper {

    EcommerceMapper INSTANCE = Mappers.getMapper(EcommerceMapper.class);

    public Categary toentity(CategaryRequestModel categaryRequestModel);
    public Product toEntity(ProductRequestModel productRequestModel);

    public CategaryResponseModel tomodel(Categary categary);
    public ProductResponseModel Tomodel(Product product);

    public User toUserEntity(UserRequestModel userRequestModel);
    public UserResponseModel tousermodel(User user);

}
