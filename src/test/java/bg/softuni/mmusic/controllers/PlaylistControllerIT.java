package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
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
public class PlaylistControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    SongRepository songRepository;

    private User user;
    private Playlist playlist;
    private Picture picture;
    private Song song;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("username2")
                .firstName("first2")
                .lastName("last2")
                .email("test2@abv.bg")
                .password("password")
                .roles(Set.of(userRoleRepository.findByRole(Role.MUSICIAN).orElseThrow()))
                .isActivated(true)
                .createdDate(LocalDate.now())
                .build();

        playlist = Playlist.builder()
                .owner(user)
                .name("playlist")
                .status(PlaylistStatus.PUBLIC)
                .build();

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

        userRepository.saveAndFlush(user);
        playlistRepository.saveAndFlush(playlist);
        songRepository.saveAndFlush(song);

    }

    @AfterEach
    public void afterEach() {
        playlistRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "username2",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void test_create_playlist() throws Exception {
        mockMvc.perform(get("/playlist/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-playlist"));
    }

    @Test
    @WithUserDetails(value = "username2",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void test_create_post() throws Exception {
        mockMvc.perform(post("/playlist/create")
                        .param("name", "playlist")
                        .param("status", "PUBLIC")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(value = "username2",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void test_failed_create() throws Exception {
        mockMvc.perform(post("/playlist/create")
                        .param("name", "p")
                        .param("status", "PUBLIC")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/playlist/create"));
    }

    @Test
    @WithUserDetails(value = "username2",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void test_get_playlist() throws Exception {
        mockMvc.perform(get("/playlist/" + playlist.getUuid()))
                .andExpect(model().attributeExists("isOwner"))
                .andExpect(model().attributeExists("songsToAdd"))
                .andExpect(model().attributeExists("playlist"))
                .andExpect(view().name("single-page-playlist"))
                .andExpect(status().isOk());

    }
}
