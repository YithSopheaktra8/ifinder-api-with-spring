package co.istad.ifinder.features.user;

import co.istad.ifinder.domain.Role;
import co.istad.ifinder.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);

    List<User> findAllByRolesAndIsBlockFalseAndIsDeleteFalse(List<Role> roles);

    Page<User> findAllByRolesAndIsDeleteFalse(List<Role> roles, Pageable pageable);

    Optional<User> findByEmailAndIsBlockFalseAndIsDeleteFalse(String email);

    Page<User> findAllByIsBlockFalseAndIsDeleteFalse(PageRequest pageRequest);

}
