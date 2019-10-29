package com.lishiyan.searchcontroller;

import com.lishiyan.api.BrandClient;
import com.lishiyan.service.SearchService1;
import com.shop.bean.Brands;
import com.shop.common.util.SpuUtils;
import com.lishiyan.bean.Goods;
import com.lishiyan.bean.SearchRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchConroller {

    @Autowired
    private SearchService1 searchService1;

    @Autowired
    private BrandClient brandClient;


    @PostMapping("/page")
    public ResponseEntity<SpuUtils<Goods>> search(@RequestBody SearchRequest searchRequest){
        System.out.println("searchRequest"+searchRequest.getKey());
        SpuUtils<Goods> resul =  searchService1.search(searchRequest);
        return ResponseEntity.ok(resul);
    }

    @GetMapping("list")
    public ResponseEntity<List<Brands>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        List<Brands> list = brandClient.queryBrandByIds(ids);
        if(list!=null){
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}
