package com.leyou.controller;


import com.leyou.service.FileService;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsServiceImpl;

    @Autowired
    private FileService fileService;


    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){
    //加载所需的数据
        Map<String,Object> modelMap = goodsServiceImpl.loadMadel(id);
        System.out.println(modelMap);
     //放入模型
        model.addAllAttributes(modelMap);

        if(!this.fileService.exists(id)){
            this.fileService.syncCreateHtml(id);
        }
        return "item";
    }




}
