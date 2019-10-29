package com.lishiyan.service;

import com.shop.bean.Brands;
import com.shop.bean.Category;
import com.shop.bean.SpecParam;
import com.lishiyan.api.BrandClient;
import com.lishiyan.api.CategoryClient;
import com.lishiyan.api.SpecificationClient;
import com.lishiyan.bean.Goods;
import com.lishiyan.bean.SearchRequest;
import com.lishiyan.bean.SearchUtils;
import com.lishiyan.dao.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class SearchService1 {
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private CategoryClient categoryClient;
    private static final Logger logger = LoggerFactory.getLogger(SearchService1.class);

    public SearchUtils search(SearchRequest searchRequest) {

        int page = searchRequest.getPage() - 1; // elasicsearch默认page是从0开始
        int size = searchRequest.getSize();
        if("null".equals(searchRequest.getKey())){
            searchRequest.setKey("");
        }
        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //0 结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        //1 分页
        queryBuilder.withPageable(PageRequest.of(page, size));
        //2 过滤
        QueryBuilder query = buildBaseQuery(searchRequest);
        queryBuilder.withQuery(query);
        //3 聚合分类品牌
        String categoryAggName = "category_agg";
        //3.1 对商品分类进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //3.2 对品牌进行聚合
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        //4 查询
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
        //5 解析结果
        long total = result.getTotalElements();
        //总页数
        long totalPage = result.getTotalPages();

        //5.2 解析聚合结果
        Aggregations aggs = result.getAggregations();
        // 获取分类聚合结果
        List<Category> categories = getCategoryAggResult(aggs.get(categoryAggName));
        //获取品牌聚合结果
        List<Brands> brands = getBrandAggResult(aggs.get(brandAggName));

        //6 完成规格参数聚合
        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size() == 1) {
            // 获取规格参数聚合
            specs = getSpecs(categories.get(0).getId(), query);
        }
        SearchUtils searchResult = new SearchUtils(total, totalPage, result.getContent(), categories, brands, specs);        // 返回结果
        return searchResult;

    }


    //构建查询
    private QueryBuilder buildBaseQuery(SearchRequest request) {
        // 创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        String key = request.getKey();
        if(StringUtils.isNotBlank(key)){
            //关键字查询
            queryBuilder.must(QueryBuilders.matchQuery("all",key));
            // 前端过滤条件
            Map<String, Object> map = request.getFilter();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                key = entry.getKey();
                // 判断
                if (!"cid3".equals(key) && ! "brandId".equals(key)) {
                    //规格参数的key
                    key = "specs." + key + ".keyword";
                }
                queryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue().toString()));
            }
        }

        return queryBuilder;
    }


    // 解析品牌聚合结果
    private List<Brands> getBrandAggResult(LongTerms terms) {
        try {

            List<Long> ids = terms.getBuckets().stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brands> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e) {
            logger.error("[搜索服务]查询品牌异常", e);
            return null;
        }
    }

    // 解析商品分类聚合结果
    private List<Category> getCategoryAggResult(LongTerms terms) {
        try {

            List<Long> ids = terms.getBuckets().stream()
                    .map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            System.out.println("ids===="+ids);
            List<Category> categories = categoryClient.selectByIdList(ids);
            System.out.println("categories==="+categories.size());
            return categories;
        } catch (Exception e) {
            logger.error("[搜索服务]查询分类异常", e);
            return null;
        }
    }

    // 聚合规格参数
    private List<Map<String, Object>> getSpecs(Long cid, QueryBuilder query) {
        try {
            // 创建集合，保存规格过滤条件
            List<Map<String, Object>> specs = new ArrayList<>();
            // 根据分类查询规格
            List<SpecParam> params =
                    this.specificationClient.getlistParamsById(null, cid, true, null);


            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);

            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));

            });

            // 查询
            AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(),Goods.class);

            Aggregations aggs = result.getAggregations();
            // 解析聚合结果
            params.forEach(param -> {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                spec.put("k", key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });

            return specs;
        }catch (Exception e){
            logger.error("[搜索服务]规格聚合出现异常：", e);
            return null;
        }
    }
}
