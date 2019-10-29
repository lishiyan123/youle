package com.shop.controller;

import com.shop.bean.Cart;
import com.shop.bean.Stock;
import com.shop.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/getStockBySkuId")
    public Stock getStockBySkuId(@RequestParam("id") Long id){
        return stockService.getStockBySkuId(id);
    }

    @RequestMapping("/decreaseStock")
    public void decreaseStock(@RequestBody List<Cart> orderDetails){
        System.out.println("orderDetails="+orderDetails);
        stockService.decreaseStock(orderDetails);
    }


}
