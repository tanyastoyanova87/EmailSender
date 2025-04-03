package app.emailsender.web.mapper;

import app.model.Email;
import app.model.EmailStatus;
import app.web.dto.EmailResponse;
import app.web.mapper.DtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DtoMapperUTest {

    @Test
    void givenEmail_whenMappedToDto_returnDto() {
        Email email = Email.builder()
                .userId(UUID.randomUUID())
                .createdOn(LocalDateTime.now())
                .subject("idk")
                .status(EmailStatus.SUCCEEDED)
                .build();

        EmailResponse response = DtoMapper.fromEmail(email);

        assertThat(email.getUserId()).isEqualTo(response.getUserId());
        assertThat(email.getStatus()).isEqualTo(response.getStatus());
        assertThat(email.getSubject()).isEqualTo(response.getSubject());
        assertThat(email.getCreatedOn()).isEqualTo(response.getCreatedOn());
    }
}
