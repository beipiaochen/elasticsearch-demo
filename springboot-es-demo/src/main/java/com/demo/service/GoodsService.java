package com.demo.service;

import com.demo.pojo.Goods;

import java.util.List;

public interface GoodsService {

    public boolean addGoods(Goods goods);

    public boolean deleteGoods(Goods goods);

    public boolean updateGoods(Goods goods);

    public List<Goods> getGoods(String queryString);

    public List<Goods> getHighGoods(String queryString);

}
