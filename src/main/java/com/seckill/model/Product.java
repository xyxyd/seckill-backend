package com.seckill.model;

import java.math.BigDecimal;

public record Product(long id, String name, String description, BigDecimal price, int stock) {
}
