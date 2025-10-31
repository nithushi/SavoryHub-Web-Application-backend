package lk.jiat.ee.savoryhubproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    // Many cart items belong to one product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Many cart items belong to one cart
    @ManyToOne
    @JsonIgnore // To prevent infinite loops when sending JSON
    @JoinColumn(name = "cart_id")
    private Cart cart;
}