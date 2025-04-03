package app.emailsender;

import app.model.Email;
import app.model.EmailStatus;
import app.service.EmailService;
import app.web.EmailController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmailController.class)
public class EmailControllerApiTest {

    @MockitoBean
    private EmailService emailService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllEmailsByParameterUserId_shouldReturnBadRequest_whenUserIdInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/emails")
                        .param("userId", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllEmailsByParameterUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        List<Email> mockEmails = List.of(
                Email.builder().userId(userId).subject("Test Subject 1").createdOn(now).status(EmailStatus.SUCCEEDED).build());


        when(emailService.getAllEmails(userId)).thenReturn(mockEmails);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/emails")
                .param("userId", userId.toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].subject").value("Test Subject 1"))
                .andExpect(jsonPath("$[0].status").value("SUCCEEDED"));
    }
}
