package com.mrsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MrFieldForceApplication {
  public static void main(String[] args) {
    SpringApplication.run(MrFieldForceApplication.class, args);
  }
}
