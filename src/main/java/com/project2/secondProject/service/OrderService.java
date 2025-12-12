package com.project2.secondProject.service;

import com.project2.secondProject.dto.OrderDTO;
import com.project2.secondProject.dto.OrderItemDTO;
import com.project2.secondProject.entity.*;
import com.project2.secondProject.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClothRepository clothRepository;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    public boolean deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }

    /**
     * Create order from user's cart.
     */
    @Transactional
    public Order createOrderFromCart(User user) {

        Cart cart = cartRepo.findByUser(user);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("COMPLETED");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem ci : cart.getItems()) {

            Cloth cloth = clothRepository.findById(ci.getCloth().getId())
                    .orElseThrow(() -> new RuntimeException("Cloth not found"));

            String size = ci.getSize();
            int cartQty = ci.getQuantity();

            Map<String, Integer> stockMap = cloth.getStockCount();
            if (stockMap == null) {
                throw new RuntimeException("Stock not available");
            }

            int available = stockMap.getOrDefault(size, 0);
            if (available < cartQty) {
                throw new RuntimeException("Insufficient stock for " + cloth.getClothName() + " (" + size + ")");
            }

            // Reduce stock
            stockMap.put(size, available - cartQty);
            cloth.setStockCount(stockMap);
            clothRepository.save(cloth);

            // Create snapshot order item
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setClothId(cloth.getId());
            oi.setClothName(cloth.getClothName());
            oi.setImageUrl(cloth.getImageUrl());
            oi.setPrice(cloth.getPrice());
            oi.setSize(size);
            oi.setQuantity(cartQty);
            oi.setSubTotal(cloth.getPrice() * cartQty);

            total += oi.getSubTotal();
            orderItems.add(oi);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // Clear cart after order
        cart.getItems().clear();
        cartRepo.save(cart);

        return saved;
    }

    /**
     * Convert Order -> OrderDTO
     */
    private OrderDTO toDTO(Order order) {

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setUsername(order.getUser() != null ? order.getUser().getUsername() : null);

        List<OrderItemDTO> itemDTOs = order.getItems()
                .stream()
                .map(oi -> {
                    OrderItemDTO d = new OrderItemDTO();

                    d.setClothId(oi.getClothId());
                    d.setClothName(oi.getClothName());
                    d.setImageUrl(oi.getImageUrl());
                    d.setPrice(oi.getPrice());
                    d.setSize(oi.getSize());
                    d.setQuantity(oi.getQuantity());
                    d.setSubTotal(oi.getSubTotal());

                    return d;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);

        return dto;
    }

    /**
     * Get all orders for a user
     */
    public List<OrderDTO> getOrdersForUser(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get all orders (admin)
     */
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get single order DTO
     */
    public OrderDTO getOrderDtoById(Long id) {
        return orderRepository.findById(id).map(this::toDTO).orElse(null);
    }

    /**
     * Get order entity (optional)
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
