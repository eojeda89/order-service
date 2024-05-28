package com.webfluxcourse.orderservice.util;

import com.webfluxcourse.orderservice.dto.*;
import com.webfluxcourse.orderservice.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static void setTransactionRequestDto(RequestContext rc) {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId(rc.getPurchaseOrderRequestDto().getUserId());
        dto.setAmount(rc.getProductDto().getPrice());
        rc.setTransactionRequestDto(dto);
    }

    public static PurchaseOrder getPurchaseOrder(RequestContext rc) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(rc.getPurchaseOrderRequestDto().getUserId());
        purchaseOrder.setProductId(rc.getPurchaseOrderRequestDto().getProductId());
        purchaseOrder.setAmount(rc.getProductDto().getPrice());
        TransactionStatus status = rc.getTransactionResponseDto().getStatus();
        OrderStatus orderStatus = TransactionStatus.APPROVED.equals(status) ? OrderStatus.COMPLETED : OrderStatus.FAILED;
        purchaseOrder.setStatus(orderStatus);
        return purchaseOrder;
    }

    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder purchaseOrder) {
        PurchaseOrderResponseDto responseDto = new PurchaseOrderResponseDto();
        BeanUtils.copyProperties(purchaseOrder, responseDto);
        responseDto.setOrderId(purchaseOrder.getId());
        return responseDto;
    }
}
