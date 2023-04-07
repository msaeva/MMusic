package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    PictureRepository pictureRepository;

    @Autowired
    private UserRepository userRepository;

    private Picture picture;
    private Song song;
    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .username("username2")
                .firstName("first2")
                .lastName("last2")
                .email("test2@abv.bg")
                .password("password")
                .roles(Set.of(userRoleRepository.findByRole(Role.USER).orElseThrow()))
                .isActivated(true)
                .createdDate(LocalDate.now())
                .build();

        userRepository.saveAndFlush(user);

        picture = Picture.builder()
                .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680273764/1390a228-ba3d-4c52-89e6-3817ae8f2c96.jpg")
                .build();

        song = Song.builder()
                .title("title")
                .Style(styleRepository.findByType(StyleType.POP).orElseThrow(NoSuchElementException::new))
                .description("description")
                .status(SongStatus.PUBLIC)
                .videoUrl("url")
                .likes(0)
                .picture(picture)
                .favouriteCount(3)
                .author(user)
                .build();

        songRepository.saveAndFlush(song);
    }

    @AfterEach
    void afterEach() {
        songRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void test_add_song() throws Exception {
        mockMvc.perform(get("/song/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-song"));
    }

    @Test
    @WithMockUser(username = "username2", roles = {"MUSICIAN"})
    public void test_add_song_with_wrong_multipart_file() throws Exception {

        mockMvc.perform(post("/song/add")
                        .param("title", "title")
                        .param("description", "description")
                        .param("status", "PUBLIC")
                        .param("image", "image")
                        .param("videoUrl", "some url")
                        .param("style", "POP")
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/song/add"));
    }

    @Test
    @WithUserDetails(value = "username2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void test_get_song() throws Exception {

        mockMvc.perform(get("/song/" + song.getUuid()))
                .andExpect(model().attributeExists("displayButtons"))
                .andExpect(model().attributeExists("favourite"))
                .andExpect(model().attributeExists("displayButtons"))
                .andExpect(model().attributeExists("song"))
                .andExpect(view().name("single-page-song"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "username2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void test_search_song() throws Exception {

        mockMvc.perform(get("/song/search")
                        .param("sort", "title:1")
                        .param("style", "POP"))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(view().name("search"))
                .andExpect(status().isOk());
    }
}
