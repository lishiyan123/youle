package com.leyou.api;

import com.shop.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-server")
public interface CategoryClient extends CategoryApi {
}
