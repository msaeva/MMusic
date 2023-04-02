package bg.softuni.mmusic.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
