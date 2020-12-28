package com.demo.controller;

import com.demo.pojo.User;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @GetMapping("/user/insert")
    public void insert(){

        User user = new User("1","xiaochen","6666","1");
        userServiceImpl.insert(user);

    }
    @GetMapping("/user/search/{searchId}")
    public List<User> search(@PathVariable String searchId){
        List<User> xiao = userServiceImpl.search(searchId);
        return xiao;
    }

}
