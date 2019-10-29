package com.lishiyan.api;

import com.shop.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-server")
public interface SpecificationClient extends SpecificationApi {
}
