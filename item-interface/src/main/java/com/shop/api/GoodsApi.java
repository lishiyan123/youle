package com.shop.api;

import com.shop.bean.Sku;
import com.shop.bean.Spu;
import com.shop.bean.SpuBo;
import com.shop.bean.SpuDetail;
import com.shop.common.util.SpuUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


public interface GoodsApi {

    /**
     * 分页查询商品
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("page")
    public SpuUtils<Spu> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable);

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */

    @GetMapping("spu/detail/{id}")
    ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id);

    /**
     * 根据spu的id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long id);

    @GetMapping("spu/querySpuById")
    ResponseEntity<Spu> querySpuById(@RequestParam("id") Long id);

    @GetMapping("sku/querySkuById")
    ResponseEntity<Sku> querySkuById(@RequestParam("id") Long id);

    @RequestMapping("sku/querySkusByIds")
    List<Sku> querySkusByIds(@RequestBody ArrayList<Long> ids);




}