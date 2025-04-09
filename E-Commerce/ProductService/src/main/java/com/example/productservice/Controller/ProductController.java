package com.example.productservice.Controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.productservice.Entity.Dto.ProductDto;
import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.Vo.ProductVo;
import com.example.productservice.Lock.RedisLock;
import com.example.productservice.Mapper.ProductMapper;
import com.example.productservice.Service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LH
 * @Date: 2025/3/17 15:32
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private RedisLock redisLock;
    @Autowired
    ProductService productService;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    private  RedisTemplate<String,Object> redisTemplate;
    private static final String LOCK_KEY = "product_query_lock"; // 锁的key
    private static final long LOCK_TIMEOUT = 3000; // 锁的超时时间
    @PostMapping("/add")
    public ProductVo insertProduct(@RequestBody ProductDto productDto){
         return productService.insertProduct(productDto);
    }
    @DeleteMapping("/delete/{id}")
    public  void deleteProduct(@PathVariable Long id){
         productMapper.deleteById(id);
    }
    @PostMapping("/alter")
    public void alterProduct(@RequestBody  ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto,product);
        productMapper.updateById(product);
    }
    @PutMapping("/status/{id}/{status}")
    public  void  updateProductStatus(@PathVariable("id") Long id , @PathVariable("status") Integer status){
         Product product =new Product();
         product.setId(id);
         product.setStatus(status);
         productMapper.updateById(product);
    }
    @GetMapping
    public List<Product> showProduct(){
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        List<Product> products = productMapper.selectList(queryWrapper);
        return products;
    }
    @GetMapping("/{id}")
    public  Product selectById(@PathVariable("id") Long id){
        String lockValue = String.valueOf(System.nanoTime());
        String id2= String.valueOf(id);
        //先查询Redis缓存
        Product product= (Product) redisTemplate.opsForValue().get(id2);
        if(product != null) return product;
        //分布式锁进行查询数据库
        if(redisLock.tryLock(LOCK_KEY,lockValue,LOCK_TIMEOUT)){
            try {
                 product = productMapper.selectById(id);
                 //设置缓存5分钟
                 redisTemplate.opsForValue().set(id2,product,300, TimeUnit.SECONDS);
            } finally {
                  redisLock.unlock(LOCK_KEY,lockValue);
            }
        }
        return product;
    }
}