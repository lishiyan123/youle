package com.lishiyan.bean;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * elasticsearch 类
 */
@Document(indexName = "goods_1", type = "docs", shards = 1, replicas = 0)
@Data
public class Goods {
    @Id
    private Long id; // spuId
    private String all; // 所有需要被搜索的信息，包含标题+分类名称+品牌名称
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 卖点
    private Long brandId;// 品牌id
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间
    private Set<Long> price;// 价格
    @Field(type = FieldType.Keyword, index = false)
    private String skus;// sku信息的json结构
    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}