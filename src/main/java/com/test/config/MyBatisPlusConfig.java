package com.test.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dwb
 * @create 2020-12-02 11:37
 */
@Configuration
@MapperScan("com.test.mapper")
@EnableTransactionManagement  //事务注解
public class MyBatisPlusConfig
{
    // 注册乐观锁插件
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    @Profile({"dev","test"})// 设置 dev test 环境开启，保证我们的效率
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();

        performanceInterceptor.setMaxTime(100); // ms设置sql执行的最大时间，如果超过了则不执行

        performanceInterceptor.setFormat(true); // 是否格式化代码
        return performanceInterceptor;
    }


}
