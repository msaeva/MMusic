package bg.softuni.mmusic.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_liked_songs")
public class UserLikedSongs extends BaseEntity {

    private String user_uuid;
    private String song_uuid;
}
