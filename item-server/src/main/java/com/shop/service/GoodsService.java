package com.shop.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.bean.*;
import com.shop.common.enums.ExceptionEnum;
import com.shop.common.exception.LyException;
import com.shop.common.util.SpuUtils;
import com.shop.mapper.SpuDetailMapper;
import com.shop.mapper.SpuMapper;
import com.shop.mapper.StockMapper;
import com.shop.mapper.skuMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoodsService {

    @Autowired
    SpuMapper spuMapper;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    skuMapper skuMapper;

    @Autowired
    SpuDetailMapper  spuDetailMapper;

    @Autowired
    StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;



    public SpuUtils<Spu> querySpuByPage(Integer page, Integer rows, String key, Boolean saleable) {

        PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }
        //默认以上一次更新时间排序
        example.setOrderByClause("last_update_time desc");

        //只查询未删除的商品
        criteria.andEqualTo("valid", 1);

        List<Spu> spus = spuMapper.selectByExample(example);

        if(CollectionUtils.isEmpty(spus)){
            throw  new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }

        loadCategoryAndBrandName(spus);
        PageInfo<Spu> objectPageInfo = new PageInfo<Spu>(spus);

        SpuUtils<Spu> spuSpuUtils = new SpuUtils<>();
        spuSpuUtils.setItems(objectPageInfo.getList());
        spuSpuUtils.setTotal(objectPageInfo.getTotal());

        return  spuSpuUtils;


    }


    private  void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            //1.查询分类
            List<Category> categories = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            //2.将分类集合转化成分类到流数据
            Stream<Category> stream = categories.stream();
            //3.获取分类名称流数据
            Stream<String> stringStream = stream.map(c -> c.getName());
            //4.将名称流数据转化成分类名称集合
            List<String> names = stringStream.collect(Collectors.toList());
            //分类名称转化成分割到字符串
            spu.setCname(StringUtils.join(names,"/"));
            //查询品牌数据
            Brands brand = brandService.queryById(spu.getBrandId());
            spu.setBname(brand.getName());

        }
    }



   /**
    * 新增商品
    * @param spu
    * @return
    */
   @Transactional
      public void saveGoods(Spu spu) {
           // 新增spu
        saveSpu(spu);
        // 新增detail
        saveSpuDetail(spu);
        // 新增sku和库存
        saveSkuAndStock(spu);

       sendMessage(spu.getId(),"insert");

    }
    private void saveSkuAndStock(Spu spu) {
        List<Stock> list = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            // 保存sku
            sku.setSpuId(spu.getId());
            // 初始化时间
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            int count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
            // 保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            list.add(stock);
        }
     /*   // 批量新增库存
        int count = stockMapper.insertList(list);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
*/
        for (Stock stock : list) {
            stockMapper.insert(stock);
        }
    }

    private void saveSpuDetail(Spu spu) {
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        int count = spuDetailMapper.insert(detail);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    private void saveSpu(Spu spu) {
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }
    }

    public SpuDetail querySpuDetailById(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

        public List<Sku> querySkuBySpuId(Long spuId) {
            // 查询sku
            Sku record = new Sku();
            record.setSpuId(spuId);
            List<Sku> skus = this.skuMapper.select(record);
            for (Sku sku : skus) {
                // 同时查询出库存
                sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
            }
            return skus;
        }

    @Transient
    public void update(SpuBo spu) {
        // 查询以前sku
        List<Sku> skus = this.querySkuBySpuId(spu.getId());
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);

        }
        // 新增sku和库存
        saveSkuAndStock(spu);

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());

        sendMessage(spu.getId(),"update");
   }

    public boolean goodsxiajia(Long id) {
        try {
            spuMapper.goodsxiajia(id);
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }

    }

    public boolean goodsshangjia(Long id) {
        try {
            spuMapper.goodsshangjia(id);
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
    }


    public Spu querySpuById(Long id) {
       return spuMapper.querySpuById(id);
    }

    private void sendMessage(Long id , String type){
       try{
       amqpTemplate.convertAndSend("item."+type,id);
       }catch (Exception e){
           System.out.println("{}商品消息发送异常，商品id：{}"+ type+ id+ e);
       }
    }


    public Sku querySkuById(Long id) {
        Sku sku = skuMapper.selectByPrimaryKey(id);
        return sku;
    }

    public List<Sku> querySkusByIds(ArrayList<Long> ids) {
       List<Sku> list = new ArrayList();
        for(Long id:ids){
            System.out.println("id="+id);
            Sku sku = skuMapper.querySkusByIds(id);
            System.out.println("sku="+sku);
            list.add(sku);
        }
        return list;

    }


}

