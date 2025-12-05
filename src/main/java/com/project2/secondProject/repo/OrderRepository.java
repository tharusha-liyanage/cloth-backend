package com.project2.secondProject.repo;

import com.project2.secondProject.entity.Order;
import com.project2.secondProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
