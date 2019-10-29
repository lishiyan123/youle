package com.payment.mapper;

import com.payment.pojo.OrderDetail;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author bystander
 * @date 2018/10/4
 */
@Component
public interface OrderDetailMapper extends Mapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
