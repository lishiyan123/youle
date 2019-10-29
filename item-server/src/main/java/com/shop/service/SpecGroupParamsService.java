package com.shop.service;


import com.shop.bean.SpecParam;
import com.shop.mapper.SpecGroupparams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupParamsService {
    @Autowired
    SpecGroupparams specGroupparams;

    /**
     * 根据id查询规格
     * @param gid
     * @return
     */
    public List<SpecParam> selectParamsByid(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        param.setGeneric(generic);

        return this.specGroupparams.select(param);
    }

    /**
     * 添加
     * @param specParam
     */
    public void saveParam(SpecParam specParam) {

        specGroupparams.insert(specParam);
    }

    /**
     * 修改
     * @param specParam
     */
    public void updateParam(SpecParam specParam) {
        specGroupparams.updateByPrimaryKey(specParam);
    }

    /**
     * 删除规格
     * @param id
     */
    public void delete(Long id) {

        specGroupparams.deleteByPrimaryKey(id);
    }
}
