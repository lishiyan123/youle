package com.shop.service;

import com.shop.bean.SpecGroup;
import com.shop.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecGroupsService {

    @Autowired
    GroupMapper groupMapper;

    public List<SpecGroup> getlist(Long cid) {

        List<SpecGroup> list=groupMapper.selectBycid(cid);
        return list;
    }

    public void save(SpecGroup specGroup) {
        groupMapper.insert(specGroup);
    }
    public void update(SpecGroup specGroup) {
        groupMapper.updateByPrimaryKey(specGroup);
    }

    public void delete(Long id) {
        groupMapper.deleteByPrimaryKey(id);
    }
}
