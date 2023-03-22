package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {

    private String title;

    @Column(nullable = false)
    private String url;

    @OneToOne
    private Song song;
}
