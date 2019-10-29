package com.shop.common.util;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class SpuUtils<T> {

    private List<T> items;
    private Long total;
    private int totalPage;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpuUtils<?> spuUtils = (SpuUtils<?>) o;
        return totalPage == spuUtils.totalPage &&
                Objects.equals(items, spuUtils.items) &&
                Objects.equals(total, spuUtils.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, total, totalPage);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public SpuUtils(List<T> items, Long total, int totalPage) {
        this.items = items;
        this.total = total;
        this.totalPage = totalPage;
    }

    public SpuUtils() {
    }
}
