package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.SongStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailedSongDto {

    private String title;

    private String description;

    private Long duration;

    private String imageUrl;

    private LocalDate createdDate;

    private bg.softuni.mmusic.model.entities.Style Style;

    private SongStatus status;

    private Integer downloadCount;

    private Integer favouriteCount;
}
