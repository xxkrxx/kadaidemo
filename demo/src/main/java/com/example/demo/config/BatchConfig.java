package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.MSTabcNEUser;

@Configuration
public class BatchConfig {

    @Bean(name = "batchConfigWriter")
    public JdbcBatchItemWriter<MSTabcNEUser> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MSTabcNEUser>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO mst_abc_ne_user (user_id, user_name) VALUES (:userId, :userName)") 
                .dataSource(dataSource)
                .build();
    }
}
