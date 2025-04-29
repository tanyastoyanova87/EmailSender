package app.emailsender.service;

import app.exception.EmailSendingException;
import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceUTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailService emailService;


    @Test
    void givenUserId_whenGetAllEmails_thenReturnListOfEmailsByUserId() {
        UUID userId = UUID.randomUUID();
        List<Email> emails = List.of(new Email(), new Email());
        when(emailRepository.findAllByUserId(userId)).thenReturn(emails);

        List<Email> allEmails = emailService.getAllEmails(userId);

        assertThat(allEmails).hasSize(2);
        assertThat(allEmails).isEqualTo(emails);

        verify(emailRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void givenEmailRequest_sendEmailAndReturnIt() {
        EmailRequest emailRequest = EmailRequest.builder()
                .userId(UUID.randomUUID())
                .body("Email")
                .sender("Sender")
                .subject("Subject")
                .email("email@gmail.com")
                .build();

        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        Email email = emailService.sendEmail(emailRequest);

        assertEquals(email.getStatus(), EmailStatus.SUCCEEDED);
        verify(emailRepository, times(1)).save(email);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void givenEmailRequest_sendEmailFails() {
        EmailRequest emailRequest = EmailRequest.builder()
                .userId(UUID.randomUUID())
                .body("Email")
                .sender("Sender")
                .subject("Subject")
                .email("email")
                .build();

        doThrow(new RuntimeException("Email cannot be sent")).when(mailSender).send(any(SimpleMailMessage.class));

        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(EmailSendingException.class, () -> emailService.sendEmail(emailRequest));
        verify(emailRepository, times(1)).save(any(Email.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}