package com.example.woowa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WoowaApplication {

  public static void main(String[] args) {
    SpringApplication.run(WoowaApplication.class, args);
  }

}
