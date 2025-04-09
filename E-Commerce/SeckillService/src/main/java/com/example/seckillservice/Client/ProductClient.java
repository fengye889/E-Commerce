package com.example.seckillservice.Client;

import com.example.seckillservice.Entity.Dto.ProductDto;
import com.example.seckillservice.Entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
 * @Author: LH
 * @Date: 2025/3/29 15:15
 */
@FeignClient(value = "ProductService",url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
    @GetMapping("/product")
    List<Product> getAllProducts();
    @PostMapping("/alter")
    void alterProduct(@RequestBody ProductDto productDto);
}
