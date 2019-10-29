package com.shop.mapper;

import com.shop.bean.SpecParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface SpecGroupparams extends Mapper<SpecParam> {
    @Select("select * from tb_spec_param where cid=#{gid}")
    List<SpecParam> selectParamsByid(@Param("gid") SpecParam gid);
}
