package com.shop.bean;

import java.util.Objects;

public class Cart {

    private Long userId;// 用户id

    private Long skuId;// 商品id

    private String title;// 标题

    private String image;// 图片

    private Long price;// 加入购物车时的价格

    private Integer num;// 购买数量

    private String ownSpec;// 商品规格参数

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                ", skuId=" + skuId +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", ownSpec='" + ownSpec + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(userId, cart.userId) &&
                Objects.equals(skuId, cart.skuId) &&
                Objects.equals(title, cart.title) &&
                Objects.equals(image, cart.image) &&
                Objects.equals(price, cart.price) &&
                Objects.equals(num, cart.num) &&
                Objects.equals(ownSpec, cart.ownSpec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, skuId, title, image, price, num, ownSpec);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getOwnSpec() {
        return ownSpec;
    }

    public void setOwnSpec(String ownSpec) {
        this.ownSpec = ownSpec;
    }

    public Cart() {
    }
}
