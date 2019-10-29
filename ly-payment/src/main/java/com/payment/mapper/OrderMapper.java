package com.payment.mapper;

import com.payment.pojo.Order;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
/**
 * @author bystander
 * @date 2018/10/4
 */
@Component
public interface OrderMapper extends Mapper<Order> {
}
