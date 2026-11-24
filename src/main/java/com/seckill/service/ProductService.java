package com.seckill.service;

import com.seckill.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final String COLUMNS = "id, name, description, price, stock";

    private final JdbcTemplate jdbc;

    private final RowMapper<Product> mapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getInt("stock")
    );

    public ProductService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Product> list() {
        return jdbc.query("select " + COLUMNS + " from product order by id", mapper);
    }

    public Product get(long id) {
        List<Product> list = jdbc.query(
                "select " + COLUMNS + " from product where id = ?", mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }
}
