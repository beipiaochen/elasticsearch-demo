package com.demo.pojo;

import org.elasticsearch.index.VersionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Document
     * indexName: 用于指点实体对应的索引名称
     * type(4.0后不推荐使用): 用于指定映射类型
     * shards: 用于指定索引的分片数
     * replicas: 用于指定副本数
     * refreshInterval: 引用的刷新间隔
     * indexStoreType: 引用的存储类型 fs
     * createIndex：是否在存储引导中创建索引 默认 true
     * versionType: 版本管理的配置
 * @Id 指明标识字段
 * @Transient 在文档中排除该字段
 * @PersistenceConstructor 指定映射的构造函数
 * @Field
 * name: 字段名
 * type: 字段类型
 *@TypeAlias 定义实体类别名
 *
 */
@Document(indexName = "goodsindex",type="goods",shards = 5,replicas=1,refreshInterval="1s",indexStoreType="fs",createIndex=true,versionType= VersionType.EXTERNAL)
@TypeAlias("goods")
public class Goods {

    @Id
    @Field(name="goodsId")
    private String id;

    @Transient
    private String uuid;

    @Field(type = FieldType.Text)
    private String goodsName;

    @Field(format = DateFormat.year_month_day)
    private String createTime;

    @Field(analyzer="ik_max_word")
    private String goodsDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String password) {
        this.uuid = uuid;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Goods() {
    }

    public Goods(String id, String uuid, String goodsName, String createTime, String goodsDesc) {
        this.id = id;
        this.uuid = uuid;
        this.goodsName = goodsName;
        this.createTime = createTime;
        this.goodsDesc = goodsDesc;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", goodsDesc='" + goodsDesc + '\'' +
                '}';
    }
}
