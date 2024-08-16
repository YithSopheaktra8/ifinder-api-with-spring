    package co.istad.ifinder.domain;

    import co.istad.ifinder.audit.Auditable;
    import co.istad.ifinder.schema.ResultSchema;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.JdbcTypeCode;
    import org.hibernate.type.SqlTypes;

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @Table(name = "scrap_links")
    @Builder
    public class ScrapUrl extends Auditable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(length = 2040)
        private String domain;

        @JdbcTypeCode(SqlTypes.JSON)
        private String urlsData;

    }
