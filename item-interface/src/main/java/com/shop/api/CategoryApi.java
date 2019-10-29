package com.shop.api;

import com.shop.bean.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("names")
     ResponseEntity<List<Category>> queryNameByIds(@RequestParam("ids") List<Long> ids);

    @RequestMapping("/selectByIdList")
    List<Category> selectByIdList(@RequestParam("cid") List<Long> cid);

}