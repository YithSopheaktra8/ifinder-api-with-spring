package co.istad.ifinder.features.history;

import co.istad.ifinder.domain.User;
import co.istad.ifinder.domain.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Integer> {

    UserHistory findByUrlContainsIgnoreCaseAndUser(String url, User user);

    Page<UserHistory> findAllByUser(Pageable pageable, User user);

    Optional<UserHistory> findByUuid(String uuid);
}
