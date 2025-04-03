package app.emailsender.service;

import app.model.Email;
import app.repository.EmailRepository;
import app.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
}