package co.istad.ifinder.domain;

import co.istad.ifinder.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "folders")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Folder extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uuid;

    private String name;

    @OneToMany(mappedBy = "folders", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks;

    @ManyToOne()
    private User user;

}
