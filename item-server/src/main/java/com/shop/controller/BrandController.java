package com.shop.controller;

import com.shop.bean.Brands;
import com.shop.common.util.BrandUtils;
import com.shop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌系类
 */
@RestController
//@CrossOrigin
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    /**
     *
     * @param page 起始页数
     * @param rows 页面数量
     * @param sortBy 是否降序
     * @param desc  排序
     * @param key 查询字段
     * @return
     */
    @GetMapping("page")
    public BrandUtils<Brands> pagelist( @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                        @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                        @RequestParam(value = "key", required = false) String key){
        return    brandService.pagelist(page,rows,sortBy,desc,key);
    }

    /**
     * 类型的添加
     * @param brands
     * @param cids
     */
    @PostMapping("saveBrand")
    public void  saveBrand(Brands brands, @RequestParam("cids")List<Long> cids){
        brandService.saveBrand(brands,cids);
    }

    /**
     * 品牌的删除
     * @param id
     * @return
     */
    @RequestMapping("deleteByid")
    public boolean deleteByid(@RequestParam("id") Long id){
       return brandService.delete(id);
    }

    /**
     * 添加
     * @param brands
     * @param cids
     */
    @PutMapping("saveBrand")
    public void  update(Brands brands, @RequestParam("cids")List<Long> cids){
        brandService.updateBrand(brands,cids);
    }

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brands>> queryBrandByCategory(@PathVariable("cid") Long cid) {
        List<Brands> list = this.brandService.queryBrandByCategory(cid);
        if(list == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping("brand/{id}")
    Brands queryBrandById(@PathVariable("id") Long id){
        return brandService.queryById(id);
    }

    @GetMapping("list")
    public ResponseEntity<List<Brands>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brands> list = this.brandService.queryBrandByIds(ids);
        if(list == null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
