package com.leyou.api;

import com.shop.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-server")
public interface BrandClient extends BrandApi {
}
