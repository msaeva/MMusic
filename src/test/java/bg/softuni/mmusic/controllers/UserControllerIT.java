package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("username")
                .firstName("first")
                .lastName("last")
                .email("test@abv.bg")
                .password("password")
                .roles(Set.of(userRoleRepository.findByRole(Role.MUSICIAN).orElseThrow()))
                .isActivated(true)
                .createdDate(LocalDate.now())
                .build();

        userRepository.saveAndFlush(user);

    }
    @AfterEach
    public void afterSetUp(){
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(
            value = "username",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getProfile() throws Exception {

        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("ownSongs"))
                .andExpect(model().attributeExists("ownPlaylists"))
                .andExpect(model().attributeExists("favouriteSongs"))
                .andExpect(view().name("auth-user-profile"));
    }

    @Test
    @WithUserDetails(
            value = "username",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void test_getUserProfile() throws Exception {
        String uuid = user.getUuid();
        mockMvc.perform(get("/user/" + uuid + "/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("songs"))
                .andExpect(model().attributeExists("playlists"))
                .andExpect(view().name("user-profile"));
    }
}
