package bg.softuni.mmusic.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
public class Song extends BaseEntity{

    @Size(min = 5, max = 12)
    @Column(nullable = false)
    private String title;

    @Size(max = 100)
    @Column()
    private String description;

    @Column(nullable = false)
    private Long duration;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    private User author;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    private Style Style;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "favourite_count")
    private Integer favouriteCount = 0;
}
