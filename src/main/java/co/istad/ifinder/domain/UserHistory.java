package co.istad.ifinder.domain;

import co.istad.ifinder.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_histories")
@Setter
@Getter
public class UserHistory extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(length = 100)
    private String url;

    @Column(length = 255)
    private String icon;

    @Column
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;
}
