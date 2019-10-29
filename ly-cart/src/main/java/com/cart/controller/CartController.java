package com.cart.controller;

import com.cart.server.CartService;
import com.shop.bean.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     *
     * @return
     */
    @PostMapping("addCart")
    public ResponseEntity<Void> addCrat(@RequestBody Cart cart,@CookieValue("LY_TOKEN") String token){
        cartService.addCart(cart,token);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车列表
     *
     * @return
     */
    @GetMapping("listCart")
    public ResponseEntity<List<Cart>> queryCartList(@CookieValue("LY_TOKEN") String token) {
        List<Cart> carts = this.cartService.queryCartList(token);
        if (carts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(carts);
    }
    /**
     * 修改购物车
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping("update")
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId,
                                          @RequestParam("num") Integer num,
                                          @CookieValue("LY_TOKEN") String token) {
        this.cartService.updateNum(skuId, num,token);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除购物车商品
     */
    @DeleteMapping("deleteCart/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId, @CookieValue("LY_TOKEN") String token) {
        this.cartService.deleteCart(skuId,token);
        return ResponseEntity.ok().build();
    }

    /**
     * 登录后购物车合并
     * @param list
     * @param token
     * @return
     */
    @PostMapping("LocalStorage")
    public ResponseEntity<Void> LocalStorage(
            @RequestBody List<Cart> list,
            @CookieValue("LY_TOKEN") String token){
        this.cartService.LocalStorage(list,token);
        return ResponseEntity.ok().build();
    }
}
