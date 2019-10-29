package com.shop.controller;

import com.shop.bean.Spu;
import com.shop.bean.SpuDetail;
import com.shop.common.util.SpuUtils;
import com.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spu")
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @GetMapping("page")
    public SpuUtils<Spu> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable){


        return goodsService.querySpuByPage(page,rows,key,saleable);


    }

    /**
     * 根据id查询规格
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id) {
        SpuDetail detail = this.goodsService.querySpuDetailById(id);
        System.out.println("detail="+detail);
        if (detail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(detail);
    }

    /**
     * 商品下架
     * @param id
     * @return
     */
    @GetMapping("/goodsxiajia/{id}")
    public boolean goodsxiajia(@PathVariable("id") Long id){
       return goodsService.goodsxiajia(id);
    }
    /**
     * 商品上架
     * @param id
     * @return
     */
    @GetMapping("/goodsshangjia/{id}")
    public boolean goodsshangjia(@PathVariable("id") Long id){
        return goodsService.goodsshangjia(id);
    }

    @GetMapping("/querySpuById")
    public ResponseEntity<Spu> querySpuById(@RequestParam("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if (spu == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }



}
