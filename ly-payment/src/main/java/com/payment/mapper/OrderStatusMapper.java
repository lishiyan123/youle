package com.payment.mapper;

import com.payment.pojo.OrderStatus;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author bystander
 * @date 2018/10/4
 */
@Component
public interface OrderStatusMapper extends Mapper<OrderStatus> {
}
