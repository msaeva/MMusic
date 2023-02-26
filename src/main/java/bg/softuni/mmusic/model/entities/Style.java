package bg.softuni.mmusic.model.entities;

import bg.softuni.mmusic.model.enums.StyleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "styles")
public class Style extends BaseEntity{

    @Size(max = 50)
    @Column()
    private String description;

    @Enumerated(EnumType.STRING)
    private StyleType type;
}
