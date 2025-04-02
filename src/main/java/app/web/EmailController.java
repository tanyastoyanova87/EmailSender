package app.web;

import app.model.Email;
import app.model.EmailStatus;
import app.service.EmailService;
import app.web.dto.EmailRequest;
import app.web.dto.EmailResponse;
import app.web.mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<EmailResponse> sendEmail(@RequestBody EmailRequest emailRequest) {
        Email email = emailService.sendEmail(emailRequest);
        EmailResponse emailResponse = DtoMapper.fromEmail(email);

        if (email.getStatus().equals(EmailStatus.FAILED)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(emailResponse);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(emailResponse);
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> getAllEmails(@RequestParam(name = "userId") UUID userId) {
        List<EmailResponse> emailResponses = emailService
                .getAllEmails(userId)
                .stream()
                .map(DtoMapper::fromEmail)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(emailResponses);
    }
}
