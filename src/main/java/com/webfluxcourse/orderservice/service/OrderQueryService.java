package com.webfluxcourse.orderservice.service;

import com.webfluxcourse.orderservice.dto.PurchaseOrderResponseDto;
import com.webfluxcourse.orderservice.repository.PurchaseOrderRepository;
import com.webfluxcourse.orderservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class OrderQueryService {
    private final PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> getOrderByUserId(int userId) {
        return Flux.fromStream(() -> orderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic())
        ;
    }
}
