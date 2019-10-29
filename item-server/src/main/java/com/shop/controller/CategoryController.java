package com.shop.controller;


import com.shop.bean.Category;
import com.shop.common.enums.ExceptionEnum;
import com.shop.common.exception.LyException;
import com.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类型系列
 */
@RestController
//@CrossOrigin
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("list")
    public List<Category> getlist(@RequestParam(name="pid",defaultValue ="0") Long pid){
        List<Category> getlist = categoryService.getlist(pid);
        if(getlist!=null && getlist.isEmpty()){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return  getlist;
    }

    /**
     * 根据商品id查询类型
     * @param id
     * @return
     */
    @GetMapping("bid")
    public List<Category> selectByCategory(@RequestParam("id") Long id){
        return  categoryService.selectByCategory(id);
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids){
        List<String > list = this.categoryService.queryNameByIds(ids);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @RequestMapping("/selectByIdList")
    public List<Category> selectByIdList(@RequestParam("cid") List<Long> cid){
        System.out.println("cid======"+cid);
        return categoryService.selectByIdList(cid);
    }


}
