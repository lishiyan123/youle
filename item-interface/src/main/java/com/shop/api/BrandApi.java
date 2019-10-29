package com.shop.api;

import com.shop.bean.Brands;
import com.shop.common.util.BrandUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("page")
    public BrandUtils<Brands> pagelist(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                       @RequestParam(value = "sortBy", required = false) String sortBy,
                                       @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                       @RequestParam(value = "key", required = false) String key);
    /**
     * 类型的添加
     * @param brands
     * @param cids
     */
    @PostMapping("saveBrand")
    public void  saveBrand(Brands brands, @RequestParam("cids") List<Long> cids);
    /**
     * 品牌的删除
     * @param id
     * @return
     */
    @RequestMapping("deleteByid")
    public boolean deleteByid(@RequestParam("id") Long id);

    /**
     * 添加
     * @param brands
     * @param cids
     */
    @PutMapping("saveBrand")
    public void  update(Brands brands, @RequestParam("cids")List<Long> cids);

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brands>> queryBrandByCategory(@PathVariable("cid") Long cid);

    @GetMapping("brand/{id}")
    Brands queryBrandById(@PathVariable("id") Long id);

    @GetMapping("list")
    List<Brands> queryBrandByIds(@RequestParam("ids") List<Long> ids);


}
