package com.project2.secondProject.service;

import com.project2.secondProject.dto.CartItemDTO;
import com.project2.secondProject.entity.*;
import com.project2.secondProject.repo.CartRepo;
import com.project2.secondProject.repo.ClothRepository;
import com.project2.secondProject.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ClothRepository clothRepo;

    public Cart getCart(int userId) {
        User user = userRepo.findById(userId).orElseThrow();
        Cart cart = cartRepo.findByUser(user);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }

        return cart;
    }

    public Cart addToCart(int userId, CartItemDTO dto) {

        Cart cart = getCart(userId);
        Cloth cloth = clothRepo.findById(dto.getClothId()).orElseThrow();

        // Check if same cloth + size exists → update quantity
        for (CartItem item : cart.getItems()) {
            if (item.getCloth().getId().equals(dto.getClothId()) &&
                    item.getSize().equals(dto.getSize())) {

                item.setQuantity(item.getQuantity() + dto.getQuantity());
                return cartRepo.save(cart);
            }
        }

        // Create new CartItem
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setCloth(cloth);
        newItem.setSize(dto.getSize());
        newItem.setQuantity(dto.getQuantity());

        cart.getItems().add(newItem);

        return cartRepo.save(cart);
    }

    public Cart removeItem(int userId, Long itemId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        return cartRepo.save(cart);
    }

    public void clearCart(int userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepo.save(cart);
    }
}
