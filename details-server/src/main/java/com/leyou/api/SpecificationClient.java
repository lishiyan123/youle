package com.leyou.api;

import com.shop.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-server")
public interface SpecificationClient extends SpecificationApi {

}
