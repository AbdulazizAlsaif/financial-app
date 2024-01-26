package com.financial.auditor.service;

import com.financial.auditor.entity.Order;
import com.financial.auditor.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public void createOrder(Order order){

        //TODO: Validation
        repository.save(order);
    }
}
