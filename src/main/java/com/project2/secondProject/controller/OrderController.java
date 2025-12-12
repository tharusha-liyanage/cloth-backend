package com.project2.secondProject.controller;

import com.project2.secondProject.dto.OrderDTO;
import com.project2.secondProject.entity.Order;
import com.project2.secondProject.entity.User;
import com.project2.secondProject.repo.UserRepo;
import com.project2.secondProject.service.JWTService;
import com.project2.secondProject.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    /** ─────────────────────────────────────────────────────────────
     *  ✔ CREATE ORDER FROM USER CART
     *  ───────────────────────────────────────────────────────────── */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUserName(token);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        try {
            Order order = orderService.createOrderFromCart(user);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /** ─────────────────────────────────────────────────────────────
     *  ✔ GET LOGGED-IN USER'S ORDERS
     *  ───────────────────────────────────────────────────────────── */
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid token");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUserName(token);

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<OrderDTO> orders = orderService.getOrdersForUser(user);
        return ResponseEntity.ok(orders);
    }

    /** ─────────────────────────────────────────────────────────────
     *  ✔ GET ALL ORDERS (ADMIN)
     *  ───────────────────────────────────────────────────────────── */
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /** ─────────────────────────────────────────────────────────────
     *  ✔ GET ORDER BY ID
     *  ───────────────────────────────────────────────────────────── */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        OrderDTO dto = orderService.getOrderDtoById(id);
        if (dto == null) {
            return ResponseEntity.badRequest().body("Order not found");
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.ok("Order deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Order not found");
        }
    }
}
