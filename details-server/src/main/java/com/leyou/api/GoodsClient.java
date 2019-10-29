package com.leyou.api;

import com.shop.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-server")
public interface GoodsClient extends GoodsApi {
}
