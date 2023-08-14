package com.example.shardingjdbcdemo.concif;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShardingJdbcExample {
    public static void main(String[] args) throws SQLException {
        ShardingConfig config = new ShardingConfig();
        DataSource dataSource = config.getShardingDataSource();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0); // 设置月份为1月份
        long timeInMillis = calendar.getTimeInMillis();

        Date date = new Date(timeInMillis);
        int update = jdbcTemplate.update("INSERT INTO t_order (order_id, user_id, create_time) VALUES (?, ?, ?)",
                123456, 7890, date);// Assume this is a date in current month

        List<Map<String, Object>> ordersByPage = getOrdersByPage("t_order",jdbcTemplate, 1, 10);
        //遍历结果
        for (Map<String, Object> order : ordersByPage) {
            System.out.println(order);
        }

    }
    public static List<Map<String, Object>> getOrdersByPage(String tableName,JdbcTemplate jdbcTemplate, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize; // 计算偏移量
        long calendar =  Calendar.getInstance().getTimeInMillis();
        Date date = new Date(calendar);
        String sql = "SELECT * FROM " + tableName + " WHERE create_time = ? LIMIT ?, ?";
        return jdbcTemplate.query(sql, new Object[]{date, offset, pageSize}, getOrderRowMapper());
    }

    private static RowMapper<Map<String, Object>> getOrderRowMapper() {
        return (resultSet, rowNum) -> {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", resultSet.getInt("order_id"));
            orderMap.put("order_id", resultSet.getString("order_id"));
            // Set other attributes...
            return orderMap;
        };
    }

}
