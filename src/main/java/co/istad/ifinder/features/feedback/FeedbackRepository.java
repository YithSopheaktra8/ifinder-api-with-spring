package co.istad.ifinder.features.feedback;

import co.istad.ifinder.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    Optional<Feedback> findByUuid(String uuid);



}
