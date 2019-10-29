package com.shop.mapper;

import com.shop.bean.Spu;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
public interface SpuMapper extends Mapper<Spu> {

    @Update("update  tb_spu  set saleable=0 where id=#{id} ")
    void goodsxiajia(Long id);
    @Update("update  tb_spu  set saleable=1 where id=#{id} ")
    void goodsshangjia(Long id);

    @Select("select * from tb_spu where id = #{id}")
    Spu querySpuById(Long id);
}
