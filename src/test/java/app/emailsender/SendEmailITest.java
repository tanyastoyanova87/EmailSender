package app.emailsender;

import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class SendEmailITest {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailSender mailSender;

    @Test
    void sendEmailWithGivenEmailRequestAndReturnEmailWithSuccessfulStatus() {
        EmailRequest emailRequest = EmailRequest.builder()
                .userId(UUID.randomUUID())
                .subject("Some subject")
                .email("email@abv.bg")
                .sender("sender@abv.bg")
                .body("some body")
                .build();

        Email email = emailService.sendEmail(emailRequest);

        assertThat(email).isNotNull();
        assertThat(email.getStatus()).isEqualTo(EmailStatus.SUCCEEDED);

        Optional<Email> savedEmail = emailRepository.findById(email.getId());
        assertThat(savedEmail).isPresent();
        assertThat(savedEmail.get().getStatus()).isEqualTo(EmailStatus.SUCCEEDED);
    }
}
