package com.example.demo.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.entity.Order;
import com.example.demo.entity.StoreProduct;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableBatchProcessing
public class DataSourceBatchConfig {

    @Value("${batch.output.file.path}")
    private String outputPath;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProductService productService;
    private final OrderService orderService;

    public DataSourceBatchConfig(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 ProductService productService,
                                 OrderService orderService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Bean
    public Job productAndOrderJob() {
        return new JobBuilder("productAndOrderJob", jobRepository)
                .start(productAndOrderStep())
                .build();
    }

    @Bean
    public Step productAndOrderStep() {
        return new StepBuilder("productAndOrderStep", jobRepository)
                .tasklet(productAndOrderTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet productAndOrderTasklet() {
        return (contribution, chunkContext) -> {
            // 商品と注文のデータを取得
            List<StoreProduct> products = productService.getAllStoreProducts();
            List<Order> orders = orderService.getAllOrders();

            // データをマップに格納
            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            data.put("orders", orders);

            // マップをJSONに変換
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data);

            // エラーハンドリング付きのファイル書き込み
            try {
                Path outputPath = Paths.get(this.outputPath);
                Files.write(outputPath, jsonData.getBytes());
            } catch (IOException e) {
                // エラーログの出力
                System.err.println("Error writing JSON data to file: " + e.getMessage());
                // 必要に応じて他の処理を追加
                throw e; // 例外を再スローしてジョブが失敗するようにする
            }

            return RepeatStatus.FINISHED;
        };
    }
}
