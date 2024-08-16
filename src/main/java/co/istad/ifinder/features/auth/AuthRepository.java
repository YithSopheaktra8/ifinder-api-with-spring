package co.istad.ifinder.features.auth;

import co.istad.ifinder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndVerificationCodeAndIsDeleteFalse(String email, String verificationCode);

}
