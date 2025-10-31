package lk.jiat.ee.savoryhubproject;

import lk.jiat.ee.savoryhubproject.entity.Product;
import lk.jiat.ee.savoryhubproject.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SavoryHubProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SavoryHubProjectApplication.class, args);
    }

    // This code will run once when the application starts
    @Bean
    CommandLineRunner runner(ProductRepository repository) {
        return args -> {
            //repository.deleteAll(); // Restart කරද්දී පරණ data clear කරනවා

//             --- Pizzas ---
//            Product p1 = new Product();
//            p1.setName("Margherita Pizza");
//            p1.setCategory("pizza"); // Use lowercase to match your folder name
//            p1.setPrice(3200.00);
//            p1.setRating(4.5);
//            // ඔබගේ folder structure එක: /static.images.categories/pizza/margherita.jpg
//            p1.setImageUrl("/images/pizza/margherita.jpg");
//
//            Product p2 = new Product();
//            p2.setName("BBQ Chicken Delight");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/BBQChickenDelight.jpg");
//
//            Product p3 = new Product();
//            p2.setName("Hawaiian Paradise");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/HawaiianParadise.jpg");
//
//            Product p4 = new Product();
//            p2.setName("Meat Feast Pizza");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/MeatFeastPizza.jpg");
//
//            Product p5 = new Product();
//            p2.setName("Mediterranean Joy");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/MediterraneanJoy.jpg");
//
//            Product p6 = new Product();
//            p2.setName("Pepperoni Pizza");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/PepperoniLovers.jpg");
//
//            Product p7 = new Product();
//            p2.setName("Veggie Supreme");
//            p2.setCategory("pizza");
//            p2.setPrice(3800.00);
//            p2.setRating(4.9);
//            p2.setImageUrl("/images/pizza/VeggieSupreme.jpg");

//             --- Burgers ---
//            Product p3 = new Product();
//            p3.setName("Classic Cheeseburger");
//            p3.setCategory("burger"); // Use lowercase to match your folder name
//            p3.setPrice(1800.00);
//            p3.setRating(4.8);
//            p3.setImageUrl("/static.images.categories/burger/classic-cheeseburger.jpg");
//
//            Product p4 = new Product();
//            p4.setName("Spicy Chicken Burger");
//            p4.setCategory("burger");
//            p4.setPrice(2100.00);
//            p4.setRating(4.7);
//            p4.setImageUrl("/static.images.categories/burger/spicy-chicken.jpg");

            //repository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7));
        };
    }

}
