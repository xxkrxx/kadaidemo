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

    // バッチジョブで出力されるファイルのパスを外部設定から取得
    @Value("${batch.output.file.path}")
    private String outputPath;

    // 必要な依存を注入 (JobRepository, トランザクションマネージャー, サービスクラス)
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ProductService productService;
    private final OrderService orderService;

    // コンストラクタで依存を注入
    public DataSourceBatchConfig(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 ProductService productService,
                                 OrderService orderService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.productService = productService;
        this.orderService = orderService;
    }

    // バッチジョブを定義するメソッド。ジョブの名前は "productAndOrderJob"
    @Bean
    public Job productAndOrderJob() {
        return new JobBuilder("productAndOrderJob", jobRepository)
                .start(productAndOrderStep()) // ジョブはステップ "productAndOrderStep" から開始
                .build();
    }

    // ステップを定義するメソッド。ステップの名前は "productAndOrderStep"
    @Bean
    public Step productAndOrderStep() {
        return new StepBuilder("productAndOrderStep", jobRepository)
                .tasklet(productAndOrderTasklet(), transactionManager) // このステップで使用するタスクレットとトランザクションマネージャーを指定
                .build();
    }

    // タスクレットを定義するメソッド。ここで実際のバッチ処理を行う
    @Bean
    public Tasklet productAndOrderTasklet() {
        return (contribution, chunkContext) -> {
            // 商品と注文のデータをサービスから取得
            List<StoreProduct> products = productService.getAllStoreProducts();
            List<Order> orders = orderService.getAllOrders();

            // データをマップに格納
            Map<String, Object> data = new HashMap<>();
            data.put("products", products); // 商品データをマップに追加
            data.put("orders", orders); // 注文データをマップに追加

            // マップをJSONに変換
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonData = objectMapper.writeValueAsString(data); // マップをJSON文字列に変換

            // エラーハンドリング付きのファイル書き込み処理
            try {
                Path outputPath = Paths.get(this.outputPath); // ファイルの出力先パスを取得
                Files.write(outputPath, jsonData.getBytes()); // JSONデータをファイルに書き込む
            } catch (IOException e) {
                // エラーログを出力
                System.err.println("Error writing JSON data to file: " + e.getMessage());
                // 例外を再スローしてジョブが失敗するようにする
                throw e;
            }

            // 処理が正常に終了したことを示す
            return RepeatStatus.FINISHED;
        };
    }
}

