package co.istad.ifinder.features.scrap;

import co.istad.ifinder.domain.ScrapeProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrapeProcessStatusRepository extends JpaRepository<ScrapeProcessStatus, Integer> {

    Optional<ScrapeProcessStatus> findByCategoryName(String categoryName);

}
