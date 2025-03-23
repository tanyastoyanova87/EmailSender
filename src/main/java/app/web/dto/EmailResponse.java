package app.web.dto;

import app.model.EmailStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailResponse {

    private String subject;

    private LocalDateTime createdOn;

    private EmailStatus status;
}
