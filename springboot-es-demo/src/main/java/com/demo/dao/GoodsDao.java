package com.demo.dao;

import com.demo.pojo.Goods;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsDao extends ElasticsearchRepository<Goods, String> {
    public <S extends Goods> S index(S var1);

    public <S extends Goods> S indexWithoutRefresh(S var1);

    public Iterable<Goods> search(QueryBuilder var1);

    public Page<Goods> search(QueryBuilder var1, Pageable var2);

    public Page<Goods> search(SearchQuery var1);

    public Page<Goods> searchSimilar(Goods var1, String[] var2, Pageable var3);

    public void refresh();

    public Class<Goods> getEntityClass();
}
