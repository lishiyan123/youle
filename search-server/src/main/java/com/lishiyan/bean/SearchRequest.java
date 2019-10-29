package com.lishiyan.bean;

import lombok.Data;

import java.util.Map;

@Data
public class SearchRequest {
    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 20;
    private String key;// 搜索条件

    private Integer page;// 当前页

    private Map<String,Object> filter;

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public Integer getPage() {
        if(page==null){
            return  DEFAULT_PAGE;
        }
        //页码校验不小于1
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getSize() {
        return DEFAULT_SIZE;
    }

}
