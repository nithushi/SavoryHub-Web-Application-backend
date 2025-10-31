package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.dto.ShippingInfoDTO;
import lk.jiat.ee.savoryhubproject.entity.*;
import lk.jiat.ee.savoryhubproject.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, CartRepository cartRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order placeOrder(String userEmail, ShippingInfoDTO shippingInfo) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cannot place an order with an empty cart.");
        }

        // --- NEW LOGIC: Save user's address if it's their first time ---
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            user.setAddress(shippingInfo.getAddress());
            user.setCity(shippingInfo.getCity());
            user.setPostalCode(shippingInfo.getPostalCode());
            userRepository.save(user); // Save the updated user details
        }

        // 1. Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setShippingAddress(shippingInfo.getAddress());
        order.setCity(shippingInfo.getCity());
        order.setPhone(shippingInfo.getPhone());

        // 2. Convert CartItems to OrderItems
        double totalAmount = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice()); // Save price at time of order
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            totalAmount += orderItem.getPrice() * orderItem.getQuantity();
        }
        order.setTotalAmount(totalAmount);

        // 3. Save the Order (and OrderItems due to CascadeType.ALL)
        Order savedOrder = orderRepository.save(order);

        // 4. Clear the user's cart
        cartRepository.delete(cart);

        return savedOrder;
    }

    // --- NEW METHOD ---
    public List<Order> getOrdersByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserId(user.getId());
    }

    // --- NEW METHOD for ADMIN: Get all orders ---
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // --- NEW METHOD for ADMIN: Update order status ---
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status); // Set the new status
        return orderRepository.save(order);
    }
}