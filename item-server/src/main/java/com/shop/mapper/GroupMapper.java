package com.shop.mapper;

import com.shop.bean.SpecGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface GroupMapper extends Mapper<SpecGroup> {
    @Select("select * from tb_spec_group where cid=#{cid}")
    List<SpecGroup> selectBycid(@Param("cid") Long cid);
}
