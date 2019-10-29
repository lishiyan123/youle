package com.shop.api;

import com.shop.bean.Cart;
import com.shop.bean.Stock;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface StockApi {

    @GetMapping("/stock/getStockBySkuId")
    Stock getStockBySkuId(@RequestParam("id") Long id);

    @RequestMapping("/stock/decreaseStock")
    void decreaseStock(@RequestBody List<Cart> orderDetails);
}
