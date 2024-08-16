package co.istad.ifinder.init;

import co.istad.ifinder.domain.Authority;
import co.istad.ifinder.domain.ScrapeProcessStatus;
import co.istad.ifinder.domain.Role;
import co.istad.ifinder.domain.User;
import co.istad.ifinder.features.scrap.ScrapeProcessStatusRepository;
import co.istad.ifinder.features.user.RoleRepository;
import co.istad.ifinder.features.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ScrapeProcessStatusRepository scrapeProcessStatusRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void initRole() {
        if (roleRepository.count() < 1) {
            Authority userRead = new Authority();
            userRead.setName("user:read");
            Authority userWrite = new Authority();
            userWrite.setName("user:write");

            Authority adminRead = new Authority();
            adminRead.setName("admin:read");
            Authority adminWrite = new Authority();
            adminWrite.setName("admin:write");

            // generate role
            Role user = new Role();
            user.setName("USER");
            user.setAuthorities(List.of(
                    userRead,
                    userWrite
            ));

            Role admin = new Role();
            admin.setName("ADMIN");
            admin.setAuthorities(List.of(
                    userWrite,
                    userRead,
                    adminRead,
                    adminWrite
            ));

            roleRepository.saveAll(List.of(user, admin));
        }
    }

    @PostConstruct
    void initAdmin() {
        if (userRepository.count() < 1) {
            // generate admin user
            User admin = new User();
            admin.setUuid(UUID.randomUUID().toString());
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setEmail("ifinder.istad@gmail.com");
            admin.setDob(LocalDate.of(1990, 1, 1));
            admin.setIsDelete(false);
            admin.setIsBlock(false);
            admin.setIsVerified(true);
            admin.setRoles(List.of(roleRepository.findByName("ADMIN").orElseThrow()));
            userRepository.save(admin);

            User pheaktra = new User();
            pheaktra.setUuid(UUID.randomUUID().toString());
            pheaktra.setFirstName("Yith");
            pheaktra.setLastName("Sopheaktra");
            pheaktra.setUsername("pheaktra");
            pheaktra.setPassword(passwordEncoder.encode("Pheaktra@123"));
            pheaktra.setEmail("yithsopheaktra18@gmail.com");
            pheaktra.setDob(LocalDate.of(1999, 1, 7));
            pheaktra.setIsDelete(false);
            pheaktra.setIsBlock(false);
            pheaktra.setIsVerified(true);
            pheaktra.setRoles(List.of(roleRepository.findByName("ADMIN").orElseThrow()));
            userRepository.save(pheaktra);

            User kdey = new User();
            kdey.setUuid(UUID.randomUUID().toString());
            kdey.setFirstName("Kdey");
            kdey.setLastName("Kdey");
            kdey.setUsername("kdey");
            kdey.setPassword(passwordEncoder.encode("Kdey@123"));
            kdey.setEmail("kdeylester@gmail.com");
            kdey.setDob(LocalDate.of(1999, 1, 7));
            kdey.setIsDelete(false);
            kdey.setIsBlock(false);
            kdey.setIsVerified(true);
            kdey.setRoles(List.of(roleRepository.findByName("ADMIN").orElseThrow()));
            userRepository.save(kdey);

        }
    }

    @PostConstruct
    void initUser() {
        if (userRepository.count() < 2) {
            // generate user
            User user = new User();
            user.setUuid(UUID.randomUUID().toString());
            user.setFirstName("User");
            user.setLastName("User");
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("User@123"));
            user.setEmail("user@gmail.com");
            user.setDob(LocalDate.of(1990, 1, 1));
            user.setIsDelete(false);
            user.setIsBlock(false);
            user.setIsVerified(true);
            user.setRoles(List.of(roleRepository.findByName("USER").orElseThrow()));
            userRepository.save(user);
        }
    }

    @PostConstruct
    void initProcessStatus() {
        if (scrapeProcessStatusRepository.count() < 1) {
            ScrapeProcessStatus scrapeProcessStatus = new ScrapeProcessStatus();
            scrapeProcessStatus.setId(1);
            scrapeProcessStatus.setLastProcessedId(0);
            scrapeProcessStatus.setLastSpiderProcessedId(1);
            scrapeProcessStatus.setIsRunning(true);
            scrapeProcessStatus.setCategoryName("");
            scrapeProcessStatusRepository.save(scrapeProcessStatus);
        }
    }
}
