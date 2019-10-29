package com.shop.mapper;

import com.shop.bean.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface CategoryMapper extends Mapper<Category> , SelectByIdListMapper<Category,Long> {
    @Select("select * from tb_category where parent_id=#{parent_id}")
    List<Category> findById(@Param("parent_id") Long parent_id);
    @Select("SELECT * from tb_category where id in (SELECT category_id FROM tb_category_brand WHERE brand_id=#{id})")
    List<Category> selectByCategory(@Param("id") Long id);

}