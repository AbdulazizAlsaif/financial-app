package com.financial.auditor.contollers;


import com.financial.auditor.entity.Order;
import com.financial.auditor.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public void createOrder(@RequestBody Order order){
        service.createOrder(order);

    }
}
