package lk.jiat.ee.savoryhubproject.repo;

import lk.jiat.ee.savoryhubproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //find by email
    Optional<User> findByEmail(String email);

}
