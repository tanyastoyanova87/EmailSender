package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailRequest {

    @NotBlank
    private UUID userId;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    @NotBlank
    private String email;

    @NotBlank
    private String sender;
}
