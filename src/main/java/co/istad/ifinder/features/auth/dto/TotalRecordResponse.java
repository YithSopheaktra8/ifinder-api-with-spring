package co.istad.ifinder.features.auth.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

public record TotalRecordResponse(
        Long totalUser,
        Long totalAdmin,
        Long totalFeedback,
        Long totalCollection
) {
}
