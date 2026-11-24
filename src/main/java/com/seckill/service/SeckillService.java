package com.seckill.service;

import com.seckill.common.BizException;
import com.seckill.model.OrderItem;
import com.seckill.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeckillService {

    private final JdbcTemplate jdbc;
    private final ProductService productService;

    public SeckillService(JdbcTemplate jdbc, ProductService productService) {
        this.jdbc = jdbc;
        this.productService = productService;
    }

    /**
     * 秒杀下单：
     * 1. 每人每个商品限购 1 件
     * 2. 通过 "stock = stock - 1 where stock > 0" 的原子更新防止超卖
     * 3. 库存扣减与订单写入在同一事务内
     */
    @Transactional
    public void seckill(long userId, long productId) {
        Product product = productService.get(productId);
        if (product == null) {
            throw new BizException("商品不存在");
        }

        Long bought = jdbc.queryForObject(
                "select count(*) from orders where user_id = ? and product_id = ?",
                Long.class, userId, productId);
        if (bought != null && bought > 0) {
            throw new BizException("您已抢购过该商品，每人限购 1 件");
        }

        int rows = jdbc.update(
                "update product set stock = stock - 1 where id = ? and stock > 0", productId);
        if (rows == 0) {
            throw new BizException("手慢了，商品已售罄");
        }

        jdbc.update(
                "insert into orders(user_id, product_id, product_name, price, status) values(?, ?, ?, ?, ?)",
                userId, productId, product.name(), product.price(), "SUCCESS");
    }

    public List<OrderItem> listOrders(long userId) {
        return jdbc.query(
                "select id, product_name, price, status, "
                        + "date_format(created_at, '%Y-%m-%d %H:%i:%s') as created_at "
                        + "from orders where user_id = ? order by id desc",
                (rs, rowNum) -> new OrderItem(
                        rs.getLong("id"),
                        rs.getString("product_name"),
                        rs.getBigDecimal("price"),
                        rs.getString("status"),
                        rs.getString("created_at")
                ), userId);
    }
}
