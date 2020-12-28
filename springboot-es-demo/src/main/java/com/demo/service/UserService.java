package com.demo.service;

import com.demo.pojo.User;

import java.util.List;

public interface UserService {

    public boolean insert(User user) ;
    public List<User> search(String searchContent);
//    public List<User> searchUserByWeight(String searchContent);
//    public List<User> searchUser(Integer pageNumber, Integer pageSize,String searchContent);
}
