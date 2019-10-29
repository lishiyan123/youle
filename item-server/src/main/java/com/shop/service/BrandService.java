package com.shop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.bean.Brands;
import com.shop.common.util.BrandUtils;
import com.shop.mapper.BrandMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;

    public BrandUtils<Brands> pagelist(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //启用分页
        PageHelper.startPage(page, rows);
        Example example = new Example(Brands.class);
        if (StringUtils.isNotBlank(key)) {
            //根据首字母或姓名查询
            example.createCriteria().andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        List<Brands> pagelist = brandMapper.selectByExample(example);

        BrandUtils<Brands> objectBrandUtils = new BrandUtils<Brands>();

        PageInfo<Brands> brandsPageInfo = new PageInfo<>(pagelist);
        objectBrandUtils.setItems(brandsPageInfo.getList());
        objectBrandUtils.setTotal(brandsPageInfo.getTotal());
        return objectBrandUtils;

    }

    public void saveBrand(Brands brands, List<Long> cids) {
        //添加商品
        brandMapper.insertSelective(brands);
        for (Long cid : cids) {
            //添加类型
            brandMapper.inserCategoryBrand(cid, brands.getId());
        }
    }

    public boolean delete(Long id) {

        try {
            //删除商品删除中间表根据id
            brandMapper.deleteCategoryBrand(id);
            //删除商品
            brandMapper.deleteByPrimaryKey(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void updateBrand(Brands brands, List<Long> cids) {
        //修改商品数据
        brandMapper.updateByPrimaryKeySelective(brands);
        for (Long cid : cids) {
            //删除中间表中的类型
            brandMapper.deleteCategoryBrand(brands.getId());
            //添加中间表类型
            brandMapper.inserCategoryBrand(cid, brands.getId());


        }
    }

    /**
     * 根据id查询品牌
     *
     * @param brandId
     * @return
     */
    public Brands queryById(Long brandId) {
        return brandMapper.selectByPrimaryKey(brandId);
    }

    public List<Brands> queryBrandByCategory(Long cid) {


        return this.brandMapper.queryByCategoryId(cid);
    }

    public List<Brands> queryBrandByIds(List<Long> ids) {
        return brandMapper.selectByIdList(ids);
    }
}
