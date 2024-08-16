package co.istad.ifinder.features.bookmarks;

import co.istad.ifinder.domain.Bookmark;
import co.istad.ifinder.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {

    Optional<Bookmark> findByUuid(String uuid);

    Page<Bookmark> findAllByUser(User user, PageRequest pageRequest);

}
