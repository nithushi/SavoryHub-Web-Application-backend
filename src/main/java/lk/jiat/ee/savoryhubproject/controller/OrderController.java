package lk.jiat.ee.savoryhubproject.controller;

import lk.jiat.ee.savoryhubproject.dto.ShippingInfoDTO;
import lk.jiat.ee.savoryhubproject.entity.Order;
import lk.jiat.ee.savoryhubproject.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody ShippingInfoDTO shippingInfo, Authentication authentication) {
        String email = authentication.getName();
        Order newOrder = orderService.placeOrder(email, shippingInfo);
        return ResponseEntity.ok(newOrder);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        String email = authentication.getName();
        List<Order> orders = orderService.getOrdersByUser(email);
        return ResponseEntity.ok(orders);
    }

    // --- ADMIN ONLY ENDPOINTS ---

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}