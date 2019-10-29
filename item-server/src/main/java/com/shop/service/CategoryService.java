package com.shop.service;

import com.shop.bean.Category;
import com.shop.common.enums.ExceptionEnum;
import com.shop.common.exception.LyException;
import com.shop.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> getlist(Long id) {
        return  categoryMapper.findById(id);
    }

    public List<Category> selectByCategory(Long id) {
       List<Category> list= categoryMapper.selectByCategory(id);
       return  list;
    }

    public List<Category> queryByIds(List<Long> asList) {

        List<Category> list = categoryMapper.selectByIdList(asList);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);

        }
        return  list;
    }

    public List<String> queryNameByIds(List<Long> ids) {

        Stream<Category> stream = categoryMapper.selectByIdList(ids).stream();
        List<String> collect = stream.map(Category::getName).collect(Collectors.toList());
        return collect;
    }


    public List<Category> selectByIdList(List<Long> cid) {
        List<Category> list = categoryMapper.selectByIdList(cid);
        return list;
    }
}
