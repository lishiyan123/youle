package com.payment.api;

import com.shop.api.StockApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-server")
public interface StockClient extends StockApi {
}
