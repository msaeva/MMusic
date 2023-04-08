package bg.softuni.mmusic.model.entities;

import bg.softuni.mmusic.model.enums.PlaylistStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaylistStatus status;

    public Set<Song> getSongs() {
        return this.getPlaylistSongs().stream().map(PlaylistSongs::getSong).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", owner=" + owner +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return name.equals(playlist.name) && status == playlist.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }
}
