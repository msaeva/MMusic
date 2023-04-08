package bg.softuni.mmusic.model.entities;

import bg.softuni.mmusic.model.enums.SongStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "songs")
public class Song extends BaseEntity {

    @Size(min = 3, max = 30)
    @Column(nullable = false)
    private String title;

    @Size(max = 500)
    @Column()
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    private Style Style;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Picture picture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "song", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @Column
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private SongStatus status;

    @PositiveOrZero
    @Column
    private Integer likes = 0;

    @Column(name = "favourite_count")
    @PositiveOrZero
    private Integer favouriteCount = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(getUuid(), song.getUuid()) && Objects.equals(title, song.title);

    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), title);
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", createdDate=" + createdDate +
                ", status=" + status +
                '}';
    }
}
