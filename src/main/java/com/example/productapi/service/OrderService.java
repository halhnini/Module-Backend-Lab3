package com.example.productapi.service;

import com.example.productapi.dto.OrderDto;
import com.example.productapi.dto.OrderItemDto;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.model.Order;
import com.example.productapi.model.OrderItem;
import com.example.productapi.model.Product;
import com.example.productapi.repository.OrderItemRepository;
import com.example.productapi.repository.OrderRepository;
import com.example.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ProductRepository productRepo;

    @Transactional
    public OrderDto create(OrderDto dto) {
        Order order = orderRepo.save(new Order());
        for (OrderItemDto itemDto : dto.items()) {
            Product product = productRepo.findById(itemDto.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'id: " + itemDto.productId()));
            OrderItem item = orderItemRepo.save(new OrderItem(null, product, itemDto.quantity(), order));
            order.getItems().add(item);
        }
        return toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDto getById(Long id) {
        return toDto(orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'id: " + id)));
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(order.getId(), order.getOrderDate(), order.getItems().stream()
                .map(i -> new OrderItemDto(i.getId(), i.getProduct().getId(), i.getQuantity()))
                .toList());
    }
}
