package com.project2.secondProject.repo;

import com.project2.secondProject.entity.Cart;
import com.project2.secondProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
