package com.seckill.service;

import com.seckill.common.BizException;
import com.seckill.model.LoginResult;
import com.seckill.util.PasswordUtil;
import com.seckill.util.TokenStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final JdbcTemplate jdbc;
    private final TokenStore tokenStore;

    public AuthService(JdbcTemplate jdbc, TokenStore tokenStore) {
        this.jdbc = jdbc;
        this.tokenStore = tokenStore;
    }

    public void register(String username, String password) {
        String name = username == null ? "" : username.trim();
        if (name.length() < 3 || name.length() > 20) {
            throw new BizException("用户名长度需为 3-20 个字符");
        }
        if (password == null || password.length() < 6) {
            throw new BizException("密码长度不能少于 6 位");
        }

        Long count = jdbc.queryForObject(
                "select count(*) from users where username = ?", Long.class, name);
        if (count != null && count > 0) {
            throw new BizException("用户名已被注册");
        }

        String salt = PasswordUtil.newSalt();
        jdbc.update("insert into users(username, password, salt) values(?, ?, ?)",
                name, PasswordUtil.encode(password, salt), salt);
    }

    public LoginResult login(String username, String password) {
        String name = username == null ? "" : username.trim();
        if (name.isEmpty() || password == null || password.isEmpty()) {
            throw new BizException("用户名和密码不能为空");
        }

        List<Map<String, Object>> rows = jdbc.queryForList(
                "select id, username, password, salt from users where username = ?", name);
        if (rows.isEmpty()) {
            throw new BizException("用户名或密码错误");
        }

        Map<String, Object> row = rows.get(0);
        String salt = (String) row.get("salt");
        String encoded = (String) row.get("password");
        if (!PasswordUtil.matches(password, salt, encoded)) {
            throw new BizException("用户名或密码错误");
        }

        long userId = ((Number) row.get("id")).longValue();
        String realName = (String) row.get("username");
        return new LoginResult(tokenStore.create(userId), realName);
    }
}
