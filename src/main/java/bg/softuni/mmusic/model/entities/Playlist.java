package bg.softuni.mmusic.model.entities;

import bg.softuni.mmusic.model.enums.PlaylistStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @Size(min = 4, max = 20)
    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "playlist")
    private Set<PlaylistSongs> playlistSongs;

    @ManyToOne
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaylistStatus status;

    public Set<Song> getSongs() {
        return this.getPlaylistSongs().stream().map(PlaylistSongs::getSong).collect(Collectors.toSet());
    }
}
