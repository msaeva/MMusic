package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StyleSeeder implements CommandLineRunner {

    private final StyleRepository styleRepository;

    public StyleSeeder(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (styleRepository.count() == 0){
            Style pop = Style.builder()
                    .type(StyleType.POP)
                    .parent(null)
                    .description("description").build();

            Style dancePop = Style.builder()
                    .type(StyleType.DANCE_POP)
                    .parent(pop)
                    .description("description").build();

            Style rock = Style.builder()
                    .type(StyleType.ROCK)
                    .parent(null)
                    .description("description").build();

            Style modernRock = Style.builder()
                    .type(StyleType.MODERN_ROCK)
                    .parent(rock)
                    .description("description").build();

            Style hiphop = Style.builder()
                    .type(StyleType.HIPHOP)
                    .parent(null)
                    .description("description").build();

            Style melodicRap = Style.builder()
                    .type(StyleType.MELODIC_RAP)
                    .parent(hiphop)
                    .description("description").build();

            styleRepository.saveAllAndFlush(List.of(pop, dancePop, rock, modernRock,
                    hiphop, melodicRap));
        }
    }
}
