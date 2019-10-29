package com.lishiyan.bean;

import com.shop.bean.Brands;
import com.shop.bean.Category;
import com.shop.common.util.SpuUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class SearchUtils extends SpuUtils<Goods> {
    private List<Category> categories;

    private List<Brands> brands;

    private List<Map<String, Object>> specs; // 规格参数过滤条件

    public SearchUtils(Long total, Long totalPage, List<Goods> items,
                       List<Category> categories, List<Brands> brands,
                       List<Map<String, Object>> specs) {
        super(items,total, Math.toIntExact(totalPage));
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
