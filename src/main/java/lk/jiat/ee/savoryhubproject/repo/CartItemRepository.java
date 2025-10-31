package lk.jiat.ee.savoryhubproject.repo;

import lk.jiat.ee.savoryhubproject.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}