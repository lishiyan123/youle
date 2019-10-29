package com.shop.mapper;

import com.shop.bean.Brands;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Component
public interface BrandMapper extends Mapper<Brands> , SelectByIdListMapper<Brands,Long> {
    @Insert("insert into tb_category_brand set category_id=#{cid},brand_id=#{id}")
    void inserCategoryBrand(@Param("cid") Long cid,@Param("id")Long id);
    @Delete("delete from tb_category_brand where brand_id=#{id}")
    void deleteCategoryBrand(@Param("id") Long id);
    @Select("SELECT b.* FROM tb_category_brand cb LEFT JOIN tb_brand b ON cb.brand_id = b.id WHERE cb.category_id = #{cid}")
    List<Brands> queryByCategoryId(Long cid);

}
