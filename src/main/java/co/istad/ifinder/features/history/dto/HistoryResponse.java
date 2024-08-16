package co.istad.ifinder.features.history.dto;

import jakarta.persistence.Column;
import org.threeten.bp.LocalDate;

import java.time.LocalDateTime;

public record HistoryResponse(
       String uuid,
       String url,
       String icon,
       String title,
       LocalDateTime createdAt,
       LocalDateTime lastModifiedAt
) {
}
