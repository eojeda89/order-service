package com.webfluxcourse.orderservice.service;

import com.webfluxcourse.orderservice.client.ProductClient;
import com.webfluxcourse.orderservice.client.UserClient;
import com.webfluxcourse.orderservice.dto.PurchaseOrderRequestDto;
import com.webfluxcourse.orderservice.dto.PurchaseOrderResponseDto;
import com.webfluxcourse.orderservice.dto.RequestContext;
import com.webfluxcourse.orderservice.repository.PurchaseOrderRepository;
import com.webfluxcourse.orderservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentService {
    private final ProductClient productClient;
    private final UserClient userClient;
    private final PurchaseOrderRepository orderRepository;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                //.map(orderRepository::save) TODO fix enum and mysql problem
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());

    }

    private Mono<RequestContext> productRequestResponse(RequestContext rc) {
        return productClient.getProductById(rc.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(rc::setProductDto)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(rc);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext rc) {
        return userClient.doTransaction(rc.getTransactionRequestDto())
                .doOnNext(rc::setTransactionResponseDto)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(rc);
    }

}
