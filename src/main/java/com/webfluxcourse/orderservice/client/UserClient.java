package com.webfluxcourse.orderservice.client;

import com.webfluxcourse.orderservice.dto.TransactionRequestDto;
import com.webfluxcourse.orderservice.dto.TransactionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserClient {
    private final WebClient webClient;

    public UserClient(@Value("${user.service.url}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public Mono<TransactionResponseDto> doTransaction(TransactionRequestDto requestDto) {
        return webClient.post().uri("transaction").bodyValue(requestDto).retrieve().bodyToMono(TransactionResponseDto.class);
    }
}
