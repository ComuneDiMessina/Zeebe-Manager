package it.almaviva.zeebe.manager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ZeebeManagerApplication {
    public static void main(final String[] args) {
      SpringApplication.run(ZeebeManagerApplication.class, args);
      }
}