package com.lishiyan;

import com.shop.bean.Spu;
import com.shop.common.util.SpuUtils;
import com.lishiyan.api.GoodsClient;
import com.lishiyan.bean.Goods;
import com.lishiyan.bean.SearchRequest;
import com.lishiyan.dao.GoodsRepository;
import com.lishiyan.service.IndexService;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ElasticsearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private IndexService indexService;

    @Test
    public void createIndex(){
        // 创建索引
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 配置映射
        this.elasticsearchTemplate.putMapping(Goods.class);
    }
    @Test
    public void loadData(){
        int page = 1; //当前页数
        int rows = 10; //每次查10数据
        int size = 0; //剩余的数据
        do{
            //查询spu
            SpuUtils<Spu> result =
                    this.goodsClient.querySpuByPage(page, rows, null, true);

            List<Spu> items = result.getItems();
            List<Goods> list = new ArrayList<>();
            for (Spu spu : items) {
                System.out.println(spu);
                Goods goods = indexService.buildGoods(spu);
                list.add(goods);
            }
            //保存到es中
            goodsRepository.saveAll(list);

            size = items.size();

            page++;

        }while (size == 10);

    }

    @Test
    public void testFind(){
        SearchRequest request=new SearchRequest();
        String key = "手机";


        Integer page = request.getPage() - 1;//page 从0开始
        Integer size = request.getSize();

        // 1.创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //2.查询
        //2.1.结果进行筛选
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
        //2.2.基本查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",key));
        //2.3.分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //3.返回结果
        Page<Goods> result = this.goodsRepository.search(queryBuilder.build());
        //4.解析结果
        long total = result.getTotalElements();
        long totalpage=(total + size-1)/size;
        System.out.println(result.getContent());
    }
}