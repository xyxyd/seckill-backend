package com.seckill.controller;

import com.seckill.common.ApiResponse;
import com.seckill.common.BizException;
import com.seckill.model.OrderItem;
import com.seckill.service.SeckillService;
import com.seckill.util.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final SeckillService seckillService;
    private final TokenStore tokenStore;

    public OrderController(SeckillService seckillService, TokenStore tokenStore) {
        this.seckillService = seckillService;
        this.tokenStore = tokenStore;
    }

    @PostMapping("/seckill/{productId}")
    public ApiResponse<Void> seckill(@PathVariable long productId,
                                     @RequestHeader(value = "X-Token", required = false) String token) {
        long userId = requireLogin(token);
        seckillService.seckill(userId, productId);
        return ApiResponse.ok();
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderItem>> orders(
            @RequestHeader(value = "X-Token", required = false) String token) {
        return ApiResponse.ok(seckillService.listOrders(requireLogin(token)));
    }

    private long requireLogin(String token) {
        Long userId = tokenStore.getUserId(token);
        if (userId == null) {
            throw new BizException(401, "登录状态已失效，请重新登录");
        }
        return userId;
    }
}
