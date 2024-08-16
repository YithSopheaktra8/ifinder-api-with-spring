package co.istad.ifinder.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "process_status")
@Getter
@Setter
public class ScrapeProcessStatus {
    @Id
    private Integer id;
    private Integer lastProcessedId;
    private Boolean isRunning;
    private String categoryName;
    private Integer lastSpiderProcessedId;
}
