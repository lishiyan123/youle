package com.payment.api;

import com.shop.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient("item-server")
@Component
public interface GoodsClient extends GoodsApi {
}
