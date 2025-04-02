package app.web.dto;

import app.model.EmailStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EmailResponse {

    private UUID userId;

    private String subject;

    private LocalDateTime createdOn;

    private EmailStatus status;
}
