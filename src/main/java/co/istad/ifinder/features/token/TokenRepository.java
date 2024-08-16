package co.istad.ifinder.features.token;

import co.istad.ifinder.domain.Token;
import co.istad.ifinder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByUser(User user);

    @Modifying
    void deleteByUser(User user);
}
