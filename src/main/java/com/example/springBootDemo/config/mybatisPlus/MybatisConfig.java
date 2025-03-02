package com.example.springBootDemo.config.mybatisPlus;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @所属模块 配置-数据库-数据库分页配置
 * @描述 <p>用于校验文件头部信息，判断文件类型<p/>
 * @创建人 xiaoYe
 * @创建时间
 * @备注
 */
@Configuration
@MapperScan("com.example.springBootDemo.dao.*")
//@Import(value = { com.example.springBootDemo.monitor.cat.mybatis.SpringCloudCatMybatisConfig.class })
public class MybatisConfig {

    /**
     * mybatis-plus分页插件<br>
     * 文档：https://mp.baomidou.com/guide/page.html <br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//    @Bean
//    public GlobalConfiguration globalConfiguration() {
//        GlobalConfiguration global = new GlobalConfiguration();
//        global.setDbType("mysql");
//        return global;
//    }
//
//    /**
//     * 配置mybatis的分页插件pageHelper
//     * @return
//     */
//    @Bean
//    public PageHelper pageHelper(){
//        PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        properties.setProperty("offsetAsPageNum","true");
//        properties.setProperty("rowBoundsWithCount","true");
//        properties.setProperty("reasonable","true");
//        //配置mysql数据库的方言
//        properties.setProperty("dialect","mysql");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }
}