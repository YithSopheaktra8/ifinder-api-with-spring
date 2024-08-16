package co.istad.ifinder.features.collection;

import co.istad.ifinder.domain.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection,Integer> {

    Optional<Collection> findByNameContainsIgnoreCase(String title);

    Page<Collection> findAll(Pageable pageable);

    Optional<Collection> findByUuid(String uuid);

    Boolean existsByName(String name);

    Optional<Collection> findByName(String name);

    Optional<Collection> findByDomainName(String domainName);
}
