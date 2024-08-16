package co.istad.ifinder.domain;

import co.istad.ifinder.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "collections")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Collection extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private String name;

    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String description;

    @JsonIgnore
    private String thumbnail;

    @JsonIgnore
    private String logo;

    @JsonIgnore
    private String domainName;

    @JdbcTypeCode(SqlTypes.JSON)
    @JsonIgnore
    private String documents;
}
