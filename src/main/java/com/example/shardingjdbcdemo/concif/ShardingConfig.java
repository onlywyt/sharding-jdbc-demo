package com.example.shardingjdbcdemo.concif;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ShardingConfig {
    public DataSource getShardingDataSource() throws SQLException {
        // Configure actual data sources
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // Configure the first data source
        BasicDataSource dataSource1 = new BasicDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/db_0");
        dataSource1.setUsername("root");
        dataSource1.setPassword("12345678");
        dataSourceMap.put("db_0", dataSource1);
        // Configure Order table rules
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("t_order","db_0.t_order_01, db_0.t_order_02, db_0.t_order_08");

        // Configure strategies for database + table sharding
        orderTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("create_time", new MonthShardingAlgorithm()));

        // Configure sharding rules
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);

        // Get data source
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

        return dataSource;
    }
}