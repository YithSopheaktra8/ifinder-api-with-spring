package co.istad.ifinder.features.scrap;

import co.istad.ifinder.domain.ScrapUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScrapUrlRepository extends JpaRepository<ScrapUrl, Integer> {

    @Query("SELECT s FROM ScrapUrl s ORDER BY s.createdAt asc ")
    List<ScrapUrl> findAllOrderByCreatedAt();

    ScrapUrl findByDomain(String domain);

    Boolean existsByDomain(String domain);

    List<ScrapUrl> findByIdGreaterThanOrderByIdAsc(Integer id);
}
