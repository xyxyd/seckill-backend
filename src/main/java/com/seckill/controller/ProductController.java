package com.seckill.controller;

import com.seckill.common.ApiResponse;
import com.seckill.common.BizException;
import com.seckill.model.Product;
import com.seckill.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ApiResponse<List<Product>> list() {
        return ApiResponse.ok(productService.list());
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> detail(@PathVariable long id) {
        Product product = productService.get(id);
        if (product == null) {
            throw new BizException("商品不存在");
        }
        return ApiResponse.ok(product);
    }
}
