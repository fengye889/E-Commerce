package com.example.productservice.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.Entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:37
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
