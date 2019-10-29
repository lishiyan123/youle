package com.shop.controller;

import com.shop.bean.Sku;
import com.shop.bean.Spu;
import com.shop.bean.SpuBo;
import com.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GoodController {

    @Autowired
    GoodsService goodsService;

    /**
     * 新增商品
     * @param spu
     * @return
     */
    @PostMapping("goods")   // json对象加上@RequestBody
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        List<Sku> skus = this.goodsService.querySkuBySpuId(id);
        if (skus == null || skus.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(skus);
    }

    @GetMapping("sku/querySkuById")
    public ResponseEntity<Sku> querySkuById(@RequestParam("id") Long id){
        Sku sku = goodsService.querySkuById(id);
        if (sku == null ) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }

    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spu) {
        try {
            this.goodsService.update(spu);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping("sku/querySkusByIds")
    List<Sku> querySkusByIds(@RequestBody ArrayList<Long> ids){
        return goodsService.querySkusByIds(ids);
    }



}
