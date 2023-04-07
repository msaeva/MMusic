package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.entities.Comment;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.*;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    private StyleRepository styleRepository;

    private Comment comment;
    private User user;
    private Song song;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username("username3")
                .firstName("first3")
                .lastName("last3")
                .email("test3@abv.bg")
                .password("password")
                .roles(Set.of(userRoleRepository.findByRole(Role.MUSICIAN).orElseThrow()))
                .isActivated(true)
                .createdDate(LocalDate.now())
                .build();

        userRepository.saveAndFlush(user);

        song = Song.builder()
                .title("title")
                .Style(styleRepository.findByType(StyleType.POP).orElseThrow(NoSuchElementException::new))
                .description("description")
                .status(SongStatus.PUBLIC)
                .videoUrl("url")
                .likes(0)
                .favouriteCount(3)
                .author(user)
                .build();

        songRepository.saveAndFlush(song);

        comment = Comment.builder()
                .text("my comment")
                .created(LocalDateTime.now())
                .song(song)
                .author(user)
                .build();

        commentRepository.saveAndFlush(comment);
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        songRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(
            value = "username3",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    public void test_GetComment() throws Exception {

        mockMvc.perform(get("/song/123/comments/" + comment.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("my comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("username3"));

    }

    @Test
    @WithUserDetails(
            value = "username3",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    public void test_getSongComments() throws Exception {
        mockMvc.perform(get("/song/" + song.getUuid() + "/comments"))
                .andExpect(status().isOk());

    }
}
