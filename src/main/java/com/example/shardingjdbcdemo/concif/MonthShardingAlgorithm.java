package com.example.shardingjdbcdemo.concif;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class MonthShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    private SimpleDateFormat formatter = new SimpleDateFormat("MM");

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        String month = formatter.format(shardingValue.getValue());
        for (String each : availableTargetNames) {
            if (each.endsWith(month)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}
