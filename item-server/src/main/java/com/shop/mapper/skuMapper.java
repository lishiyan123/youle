package com.shop.mapper;


import com.shop.bean.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public interface skuMapper extends Mapper<Sku>{

    @Select("select * from tb_sku where id = #{id}")
    Sku querySkusByIds(@Param("id") Long id);
}
