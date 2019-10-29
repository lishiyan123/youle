package com.shop.controller;

import com.shop.bean.SpecGroup;
import com.shop.service.SpecGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类规格
 */
@RestController
@RequestMapping("spec")
public class SpecGroupController {

    @Autowired
    SpecGroupsService groupsService;



    /**
     * 根据分类id查询 规格
     * @param cid
     * @return
     */
    @GetMapping("/groups/{cid}")
    public List<SpecGroup> getlist(@PathVariable Long cid){
      List<SpecGroup> list= groupsService.getlist(cid);
       return  list;
    }

    /**
     * 添加
     * @param specGroup
     */
    @PostMapping("group")
    public void  save(@RequestBody  SpecGroup specGroup){
        groupsService.save(specGroup);
    }

    /**
     * 修改
     * @param specGroup
     */
    @PutMapping("group")
    public void  update(@RequestBody  SpecGroup specGroup){
        groupsService.update(specGroup);
    }
    @DeleteMapping("/group/{id}")
    public void  delete(@PathVariable Long id){
        groupsService.delete(id);

    }


}
