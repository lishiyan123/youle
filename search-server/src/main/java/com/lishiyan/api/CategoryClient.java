package com.lishiyan.api;

import com.shop.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-server")
public interface CategoryClient extends CategoryApi {
}