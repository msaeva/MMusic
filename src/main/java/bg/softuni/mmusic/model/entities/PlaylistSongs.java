package bg.softuni.mmusic.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playlists_songs")
public class PlaylistSongs extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "playlist_uuid", referencedColumnName = "uuid")
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "song_uuid", referencedColumnName = "uuid")
    private Song song;
}
