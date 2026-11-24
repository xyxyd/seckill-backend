package com.seckill.model;

import java.math.BigDecimal;

public record OrderItem(long id, String productName, BigDecimal price, String status, String createdAt) {
}
