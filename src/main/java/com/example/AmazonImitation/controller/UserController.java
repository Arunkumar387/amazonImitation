package com.example.AmazonImitation.controller;

import com.example.AmazonImitation.model.GitModel;
import com.example.AmazonImitation.model.UserRequestModel;
import com.example.AmazonImitation.model.UserResponseModel;
import com.example.AmazonImitation.service.UserServices;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping("/saveuser")
    public UserResponseModel saveuser(UserRequestModel userRequestModel)
    {
        return userServices.toUserEntity(userRequestModel);
    }

    @PostMapping
    public boolean authentication(String user,String password){
        return userServices.authenticate(user,password);
    }

    @GetMapping("/git")
    public void completeGitProcess(@RequestBody GitModel gitModel) throws IOException, GitAPIException {
        userServices.completeGitProcess(gitModel);
    }
}
