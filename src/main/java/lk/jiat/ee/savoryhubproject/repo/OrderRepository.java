package lk.jiat.ee.savoryhubproject.repo;

import lk.jiat.ee.savoryhubproject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders for a specific user ID
    List<Order> findByUserId(Long userId); // <-- මේ method එක එකතු කරන්න
    long countByStatus(String status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o") // <-- මේ method එක එකතු කරන්න
    Double getTotalRevenue();
}