package com.example.productservice.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.productservice.Entity.Dto.ProductDto;
import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.Vo.ProductVo;
import com.example.productservice.Mapper.ProductMapper;
import com.example.productservice.Service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:38
 */
@Service
public class ProductServiceimpl implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public ProductVo insertProduct(ProductDto productDto) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        ProductVo productVo = new ProductVo();
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        System.out.println(product);
        wrapper.eq(Product::getName,productDto.getName());
        Product product1 = productMapper.selectOne(wrapper);
        //判断该商品名是否已经存在
        if( product1 != null){
            productVo.setCode("0");
            productVo.setMsg("商品名已经存在");
            return  productVo;
        }
        productMapper.insert(product);
        Product product2=productMapper.selectOne(wrapper);
        //封装Vo
        productVo.setId(product2.getId());
        productVo.setName(product2.getName());
        productVo.setCode("1");
        productVo.setMsg("商品添加成功");
        return  productVo;

    }
}