package com.example.config;

import javax.activation.DataSource;
import javax.swing.text.html.parser.Entity;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "com.example.domain.db")
public class MyBatisConfig {
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
	SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	sessionFactory.setDataSource((javax.sql.DataSource) dataSource);
	sessionFactory.setTypeAliasesPackage(Entity.class.getPackage().getName());
	sessionFactory.setConfigLocation(new ClassPathResource("/mybatis-config.xml"));
	return sessionFactory;
    }
}
