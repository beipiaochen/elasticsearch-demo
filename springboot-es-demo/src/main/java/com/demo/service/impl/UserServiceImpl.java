package com.demo.service.impl;


import com.demo.dao.UserDao;
import com.demo.pojo.User;
import com.demo.service.UserService;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public boolean insert(User user) {
        boolean falg=false;
        try{
            userDao.save(user);
            falg=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return falg;
    }

    @Override
    public List<User> search(String searchContent) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(searchContent);
        System.out.println("查询的语句:"+builder);
        Iterable<User> searchResult = userDao.search(builder);
        Iterator<User> iterator = searchResult.iterator();
        List<User> list=new ArrayList<User>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }
}
