package com.demo.dao;

import com.demo.pojo.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserDao extends ElasticsearchRepository<User, Long> {

}