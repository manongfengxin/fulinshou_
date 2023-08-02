package com.lb.fulinshou.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("com.lb.fulinshou.mapper")    //扫描mapper包
@EnableTransactionManagement //启用事务管理
public class MybatisPlusConfig {
}
