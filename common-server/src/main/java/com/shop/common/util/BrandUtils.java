package com.shop.common.util;

import lombok.Data;

import java.util.List;

@Data
public class BrandUtils<T> {
    private List<T> items;
    private Long total;
    private Long totalPage;

}
