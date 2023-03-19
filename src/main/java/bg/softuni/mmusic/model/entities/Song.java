package bg.softuni.mmusic.model.entities;

import bg.softuni.mmusic.model.enums.SongStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseEntity {

    @Size(min = 5, max = 12)
    @Column(nullable = false)
    private String title;

    @Size(max = 100)
    @Column()
    private String description;

    @Column(nullable = false)
    private Long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    private Style Style;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Picture picture;

    @Enumerated(EnumType.STRING)
    private SongStatus status;

    @PositiveOrZero
    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @PositiveOrZero
    @Column
    private Integer likes = 0;

    @Column(name = "favourite_count")
    @PositiveOrZero
    private Integer favouriteCount = 0;
}
