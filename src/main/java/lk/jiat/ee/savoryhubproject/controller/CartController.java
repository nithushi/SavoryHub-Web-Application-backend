package lk.jiat.ee.savoryhubproject.controller;

import lk.jiat.ee.savoryhubproject.dto.AddToCartRequestDTO;
import lk.jiat.ee.savoryhubproject.dto.UpdateCartRequestDTO;
import lk.jiat.ee.savoryhubproject.entity.Cart;
import lk.jiat.ee.savoryhubproject.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Endpoint to get the user's cart
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String email = authentication.getName();
        Cart cart = cartService.getCartByUserEmail(email);
        return ResponseEntity.ok(cart);
    }

    // Endpoint to add an item to the cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestBody AddToCartRequestDTO request, Authentication authentication) {
        String email = authentication.getName();
        Cart updatedCart = cartService.addItemToCart(email, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    // --- NEW ENDPOINT to UPDATE QUANTITY ---
    @PutMapping("/update")
    public ResponseEntity<Cart> updateItemQuantity(@RequestBody UpdateCartRequestDTO request, Authentication authentication) {
        String email = authentication.getName();
        Cart updatedCart = cartService.updateItemQuantity(email, request.getCartItemId(), request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }

    // --- NEW ENDPOINT to REMOVE ITEM ---
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long cartItemId, Authentication authentication) {
        String email = authentication.getName();
        Cart updatedCart = cartService.removeItemFromCart(email, cartItemId);
        return ResponseEntity.ok(updatedCart);
    }
}