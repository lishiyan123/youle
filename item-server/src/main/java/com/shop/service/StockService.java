package com.shop.service;

import com.shop.bean.Cart;
import com.shop.bean.Stock;
import com.shop.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;


    public Stock getStockBySkuId(Long id) {
        Stock stock = stockMapper.selectByPrimaryKey(id);
        System.out.println("stock="+stock);
        return stock;
    }

    public void decreaseStock(List<Cart> orderDetails) {
        for(Cart cart:orderDetails){
            System.out.println("购买数量为："+cart.getNum());
            System.out.println("购买商品数量为："+cart.getSkuId());
            stockMapper.decreaseStock(cart.getSkuId(),cart.getNum());
        }
    }
}
