package bg.softuni.mmusic.model.dtos.comment;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String uuid;
    private String text;
    private String username;
    private String createdDate;

}
