package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_favourite_songs")
public class UserFavouriteSongs extends BaseEntity{

    private String user_uuid;
    private String song_uuid;
}
