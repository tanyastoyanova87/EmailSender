package app.service;

import app.exception.EmailSendingException;
import app.model.Email;
import app.model.EmailStatus;
import app.repository.EmailRepository;
import app.web.dto.EmailRequest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    public EmailService(EmailRepository emailRepository, MailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;
    }

    public Email sendEmail(EmailRequest emailRequest) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailRequest.getEmail());
        mailMessage.setSubject(emailRequest.getSubject());
        mailMessage.setText(emailRequest.getBody());
        mailMessage.setFrom(emailRequest.getSender());

        Email email = Email.builder()
                .userId(emailRequest.getUserId())
                .subject(emailRequest.getSubject())
                .body(emailRequest.getBody())
                .createdOn(LocalDateTime.now())
                .sender(emailRequest.getSender())
                .build();

        try {
            mailSender.send(mailMessage);
            email.setStatus(EmailStatus.SUCCEEDED);
        } catch (Exception e) {
            email.setStatus(EmailStatus.FAILED);
            emailRepository.save(email);
            throw new EmailSendingException("Something went wrong. The email cannot be sent.");
        }

        return emailRepository.save(email);
    }

    public List<Email> getAllEmails(UUID userId) {
        return emailRepository.findAllByUserId(userId);
    }
}
