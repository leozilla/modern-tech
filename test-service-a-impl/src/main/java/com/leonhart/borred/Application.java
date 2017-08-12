package com.leonhart.borred;

import io.vertx.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.core.http.HttpServerResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

import static io.vertx.core.Vertx.*;

@SpringBootApplication
public class Application {

   public static void main(final String[] args) {
      SpringApplication.run(Application.class, args);
   }

   @Bean
   public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
      return args -> {
      };
   }
}
