package com.shop.bean;

import java.util.List;
import java.util.Objects;

public class PageResult<T> {

    private Long total;

    private Integer totalPage;

    private List<T> content;

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", totalPage=" + totalPage +
                ", content=" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageResult<?> that = (PageResult<?>) o;
        return Objects.equals(total, that.total) &&
                Objects.equals(totalPage, that.totalPage) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, totalPage, content);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageResult() {
    }

    public PageResult(Long total, Integer totalPage, List<T> content) {
        this.total = total;
        this.totalPage = totalPage;
        this.content = content;
    }
}
