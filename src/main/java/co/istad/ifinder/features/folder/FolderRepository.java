package co.istad.ifinder.features.folder;

import co.istad.ifinder.domain.Folder;
import co.istad.ifinder.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer>{

    Boolean existsByNameAndUser(String name, User user);

    Boolean existsByUuid(String uuid);

    Optional<Folder> findByUuid(String uuid);

    Page<Folder> findAllByUser(User user, PageRequest pageRequest);

    List<Folder> findAllByUser(User user);

}
