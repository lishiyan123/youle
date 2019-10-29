package com.cart.server;

import com.cart.api.GoodsClient;
import com.shop.bean.Cart;
import com.shop.bean.Sku;
import com.shop.bean.User;
import com.shop.common.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "ly:cart:uid:";

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart,String token) {
        System.out.println("cart="+cart);
        System.out.println("token="+token);
        //获取登录用户
        User user = JsonUtils.toBean(stringRedisTemplate.opsForValue().get(token), User.class);
        //给userid赋值
        cart.setUserId(user.getId());
        //Redis的key
        String key = KEY_PREFIX+user.getId();
        //获取hash的操作对象
        BoundHashOperations<String,Object,Object> hashOps = stringRedisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean aBoolean = hashOps.hasKey(skuId.toString());
        if(aBoolean){
            // 存在，获取购物车数据
            String s = hashOps.get(skuId.toString()).toString();
            cart = JsonUtils.toBean(s,Cart.class);
            // 修改购物车数量
            cart.setNum(cart.getNum()+num);
        }else{
            ResponseEntity<Sku> skuResponseEntity = goodsClient.querySkuById(skuId);
            if (skuResponseEntity.getStatusCode()!= HttpStatus.OK || !skuResponseEntity.hasBody()){
                logger.error("添加购物车的商品不存在：skuId:{}", skuId);
                throw new RuntimeException();
            }
            Sku sku = skuResponseEntity.getBody();
            //三目运算符 判断sku.images为空不，不为空就以逗号分割取第一个
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 将购物车数据写入redis
        System.out.println("key="+key);
        hashOps.put(cart.getSkuId().toString(), JsonUtils.toString(cart));

    }

    public List<Cart> queryCartList(String token) {
        User user = JsonUtils.toBean(stringRedisTemplate.opsForValue().get(token), User.class);
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if(!this.stringRedisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }
        // 查询购物车数据
        return carts.stream().map(o -> JsonUtils.toBean(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num, String token) {
        User user = JsonUtils.toBean(stringRedisTemplate.opsForValue().get(token), User.class);
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        // 获取购物车
        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.toBean(json, Cart.class);
        cart.setNum(num);
        // 写入购物车
        hashOps.put(skuId.toString(), JsonUtils.toString(cart));
    }


    public void deleteCart(String skuId, String token) {
        User user = JsonUtils.toBean(stringRedisTemplate.opsForValue().get(token), User.class);
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.stringRedisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }

    public void LocalStorage(List<Cart> list, String token) {
        for (Cart cart : list) {
            addCart(cart,token);
        }
    }
}
