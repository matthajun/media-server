package com.example.mediaserver;

import org.springdoc.core.configuration.SpringDocKotlinConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = SpringDocKotlinConfiguration.class)
public class MediaServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(MediaServerApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      System.out.println("Minio 연결 및 bucket 확인");
    };
  }
}
