package com.shop.controller;

import com.shop.bean.SpecParam;
import com.shop.service.SpecGroupParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规格类型
 */
@RestController
@RequestMapping("spec")
public class SepecParamsController {
    @Autowired
    SpecGroupParamsService specGroupParamsService;



    @GetMapping("/params")
    public List<SpecParam> getlistParamsById(@RequestParam(value="gid", required = false) Long gid,
                                             @RequestParam(value="cid", required = false) Long cid,
                                             @RequestParam(value="searching", required = false) Boolean searching,
                                             @RequestParam(value="generic", required = false) Boolean generic){
        List<SpecParam> getlistParamsByid= specGroupParamsService.selectParamsByid(gid,cid,searching,generic);
        System.out.println(getlistParamsByid);
        return  getlistParamsByid;
    }
    @PostMapping("param")

    public void  saveParam(@RequestBody SpecParam specParam){
        specGroupParamsService.saveParam(specParam);

    }

    /**s'g
     *
     * @param specParam
     */
    @PutMapping("param")
    public void  updateParam(@RequestBody SpecParam specParam){
        specGroupParamsService.updateParam(specParam);

    }

    @DeleteMapping("/param/{id}")
    public void  delete(@PathVariable Long id){
        specGroupParamsService.delete(id);

    }
}
