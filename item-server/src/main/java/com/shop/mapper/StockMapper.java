package com.shop.mapper;

import com.shop.bean.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;


@Component
public interface StockMapper extends Mapper<Stock>, InsertListMapper<Stock> {

    @Update("update tb_stock set stock=stock-#{num} where sku_id=#{skuId}")
    void decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
