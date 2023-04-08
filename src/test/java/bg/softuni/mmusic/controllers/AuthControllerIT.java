package bg.softuni.mmusic.controllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private GreenMail greenMail;
    @Value("${mail.host}")
    private String mailHost;
    @Value("${mail.port}")
    private Integer mailPort;
    @Value("${mail.username}")
    private String mailUsername;
    @Value("${mail.password}")
    private String mailPassword;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(mailPort, mailHost, "smtp"));
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void testRegistration() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "username")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                        .param("email", "email@examle.com")
                        .param("password", "password")
                        .param("confirmPassword", "password")
                        .param("roles", "USER")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));


        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        Assertions.assertEquals(1, receivedMessages.length);
        Assertions.assertEquals("email@examle.com", receivedMessages[0].getAllRecipients()[0].toString());
    }

    @Test
    public void testRegisterForm() throws Exception {
        mockMvc.perform(get("/users/register")
        ).andExpect(status().is2xxSuccessful());
    }

    @Test
    void testRegistrationWithError() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "username")
                        .param("firstName", "Test")
                        .param("lastName", "Testov")
                        .param("email", "email@examle.com")
                        .param("password", "testtest")
                        .param("confirmPassword", "test123")
                        .param("roles", "USER")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/users/login")
        ).andExpect(status().is2xxSuccessful());
    }


    @Test
    void testLoginUnsuccessful() throws Exception {
        mockMvc.perform(post("/users/login-error")
                        .param("username", "test")
                        .param("password", "")
                        .with(csrf())
                ).andExpect(status()
                        .is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }
}
