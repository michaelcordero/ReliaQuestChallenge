package com.example.rqchallenge.client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ReliaQuestClient {

   @Bean
   public WebClient webClient() {
      return WebClient.builder().baseUrl("https://dummy.restapiexample.com/api/v1/").build();
   };


}
