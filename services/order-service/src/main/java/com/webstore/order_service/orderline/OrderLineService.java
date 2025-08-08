package com.webstore.order_service.orderline;

import com.webstore.order_service.orderline.dto.OrderLineResponse;

import java.util.List;

public interface OrderLineService {
    List<OrderLineResponse> findAllByOrderId(Long id);
}
