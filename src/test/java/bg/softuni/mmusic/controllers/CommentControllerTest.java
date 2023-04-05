package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.comment.CommentDto;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private AuthService authService;

    @Test
    public void testGetComment() throws Exception {
        Mockito.when(commentService.getComment("uuid"))
                .thenReturn(getCommentDto());

        mockMvc.perform(MockMvcRequestBuilders.get("/song/123/comments/456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value("uuid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("my comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("author"));

    }

    private CommentDto getCommentDto() {
        return CommentDto.builder()
                .uuid("uuid")
                .text("my comment")
                .username("author")
                .createdDate("20-02-2023")
                .build();
    }
}
