package com.lishiyan.api;

import com.shop.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-server")
public interface GoodsClient extends GoodsApi {
}