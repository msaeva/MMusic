package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity{

    @Size(min = 4, max = 12)
    @Column(nullable = false)
    private String name;

    @OneToMany
    private Set<Song> songs;

    @ManyToOne
    private User owner;
}
