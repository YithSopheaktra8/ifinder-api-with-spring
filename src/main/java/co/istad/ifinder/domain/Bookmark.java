package co.istad.ifinder.domain;

import co.istad.ifinder.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookmarks")
@Setter
@Getter
public class Bookmark extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private String title;

    private String url;

    private String icon;

    @ManyToOne
    private Folder folders;

    @ManyToOne
    private User user;
}

