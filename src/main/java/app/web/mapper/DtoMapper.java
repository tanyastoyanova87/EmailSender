package app.web.mapper;

import app.model.Email;
import app.web.dto.EmailResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public EmailResponse fromEmail(Email email) {
        return EmailResponse.builder()
                .subject(email.getSubject())
                .createdOn(email.getCreatedOn())
                .status(email.getStatus())
                .build();
    }
}
