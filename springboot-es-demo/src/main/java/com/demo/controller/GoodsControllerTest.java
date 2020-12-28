package com.demo.controller;


import com.demo.pojo.Goods;
import com.demo.service.GoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsControllerTest {

//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsService goodsServiceImpl;

    @Test
    public void updateGoods(){

    }
    @Test
    public void getHighGoods(){
        List<Goods> highGoods = goodsServiceImpl.getHighGoods("桌子");
        System.out.println(highGoods);
    }

    @Test
    public void getGoods(){
        List<Goods> goodsList = goodsServiceImpl.getGoods("桌子");
        System.out.println(goodsList);
    }
    @Test
    public void addGoods(){
        Goods goods = new Goods("1", UUID.randomUUID()+"","台球桌配件",new SimpleDateFormat("yyyy-MM-dd").format(new Date()),"台球桌有很多种不同的类型，而不同种类的台球桌所对应的尺寸也有所不同，并且每种台球桌尺寸都有严格的规定，不能随意更改。台球桌分为斯诺克球桌、美式落袋、九球桌、开伦球桌等选购时必须考虑到各个方面。");
        goodsServiceImpl.addGoods(goods);
    }
}
