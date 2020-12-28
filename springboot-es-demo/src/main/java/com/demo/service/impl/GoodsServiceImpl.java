package com.demo.service.impl;

import com.demo.dao.GoodsDao;
import com.demo.pojo.Goods;
import com.demo.service.GoodsService;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public boolean addGoods(Goods goods) {
        Goods save = goodsDao.save(goods);
        return save != null;
    }

    @Override
    public boolean deleteGoods(Goods goods) {
        return false;
    }

    @Override
    public boolean updateGoods(Goods goods) {
        return false;
    }

    @Override
    public List<Goods> getGoods(String queryString) {
        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(queryString);
        Iterable<Goods> goodsIterable = goodsDao.search(queryBuilder);
        List<Goods> goodsList = new ArrayList<>();
        goodsIterable.forEach(goods -> goodsList.add(goods));
        return goodsList;
    }
    /**
     * 高亮查询
     */
    public List<Goods> getHighGoods(String queryString){

        QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(queryString);
        HighlightBuilder highlightBuilder = new HighlightBuilder();
       highlightBuilder.tagsSchema("styled");
        HighlightBuilder highlightBuilder1 = highlightBuilder.highlightQuery(queryBuilder);
        QueryBuilder queryBuilder1 = highlightBuilder1.highlightQuery();
        Iterable<Goods> goodsIterable = goodsDao.search(queryBuilder1);
        List<Goods> goodsList = new ArrayList<>();
        goodsIterable.forEach(goods -> goodsList.add(goods));
        return goodsList;
    }

}
