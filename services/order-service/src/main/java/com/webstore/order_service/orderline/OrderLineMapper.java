package com.webstore.order_service.orderline;

import com.webstore.order_service.orderline.dto.OrderLineResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderLineMapper {

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getOrder().getId(),
                orderLine.getProductId(),
                orderLine.getQuantity(),
                orderLine.getPricePerItem()
        );
    }
}
