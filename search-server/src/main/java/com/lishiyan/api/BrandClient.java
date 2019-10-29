package com.lishiyan.api;

import com.shop.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-server")
public interface BrandClient extends BrandApi {
}
