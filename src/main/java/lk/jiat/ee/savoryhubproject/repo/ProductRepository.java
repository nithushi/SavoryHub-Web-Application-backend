package lk.jiat.ee.savoryhubproject.repo;

import lk.jiat.ee.savoryhubproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA magic: This method will automatically generate a query
    // to find all products where the 'category' field matches the given string.
    List<Product> findByCategory(String category);

    // --- NEW METHOD ---
    // This will generate a query like: SELECT * FROM product WHERE name LIKE '%<name>%'
    // It finds all products where the name contains the search term, ignoring case.
    List<Product> findByNameContainingIgnoreCase(String name);
}