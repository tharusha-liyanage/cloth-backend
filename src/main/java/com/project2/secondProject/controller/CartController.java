package com.project2.secondProject.controller;

import com.project2.secondProject.dto.CartItemDTO;
import com.project2.secondProject.entity.Cart;
import com.project2.secondProject.entity.User;
import com.project2.secondProject.repo.UserRepo;
import com.project2.secondProject.service.CartService;
import com.project2.secondProject.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartService cartService;

    private int getUserIdFromToken(String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtService.extractUserName(token);
        User user = userRepo.findByUsername(username);
        return user.getId();
    }

    @GetMapping("/my")
    public Cart getMyCart(@RequestHeader("Authorization") String authHeader) {
        return cartService.getCart(getUserIdFromToken(authHeader));
    }

    @PostMapping("/add")
    public Cart addToCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CartItemDTO dto) {

        return cartService.addToCart(getUserIdFromToken(authHeader), dto);
    }

    @DeleteMapping("/remove/{itemId}")
    public Cart removeItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long itemId) {

        return cartService.removeItem(getUserIdFromToken(authHeader), itemId);
    }

    @DeleteMapping("/clear")
    public String clearCart(@RequestHeader("Authorization") String authHeader) {
        cartService.clearCart(getUserIdFromToken(authHeader));
        return "Cart cleared";
    }
}
