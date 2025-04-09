package com.example.productservice.Service;

import com.example.productservice.Entity.Dto.ProductDto;
import com.example.productservice.Entity.Vo.ProductVo;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:38
 */
public interface ProductService {
    ProductVo insertProduct(ProductDto productDto);

}
