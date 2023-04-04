package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "pictures")
public class Picture extends BaseEntity {

    private String title;

    @Column(nullable = false)
    private String url;

    @OneToOne
    private Song song;
}
