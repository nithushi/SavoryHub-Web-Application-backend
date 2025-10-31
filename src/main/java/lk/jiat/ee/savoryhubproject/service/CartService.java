package lk.jiat.ee.savoryhubproject.service;

import lk.jiat.ee.savoryhubproject.entity.Cart;
import lk.jiat.ee.savoryhubproject.entity.CartItem;
import lk.jiat.ee.savoryhubproject.entity.Product;
import lk.jiat.ee.savoryhubproject.entity.User;
import lk.jiat.ee.savoryhubproject.repo.CartRepository;
import lk.jiat.ee.savoryhubproject.repo.ProductRepository;
import lk.jiat.ee.savoryhubproject.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Cart getCartByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(String email, Long productId, int quantity) {
        Cart cart = getCartByUserEmail(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item is already in cart
        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartRepository.save(cart);
            }
        }

        // If not, add as a new item
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        cart.getCartItems().add(newCartItem);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(String email, Long cartItemId, int newQuantity) {
        Cart cart = getCartByUserEmail(email);

        if (newQuantity <= 0) {
            return removeItemFromCart(email, cartItemId);
        }

        cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(newQuantity));

        return cartRepository.save(cart);
    }

    // --- NEW METHOD to REMOVE ITEM ---
    @Transactional
    public Cart removeItemFromCart(String email, Long cartItemId) {
        Cart cart = getCartByUserEmail(email);
        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        return cartRepository.save(cart);
    }
}