package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleSeeder {

    private final StyleRepository styleRepository;

    public StyleSeeder(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }


    public void init() {

        if (styleRepository.count() == 0) {
            Style pop = Style.builder()
                    .type(StyleType.POP)
                    .parent(null)
                    .description("Pop music is a genre of popular music that originated " +
                            "in its modern form during the mid-1950s in the" +
                            " United States.").build();

            Style dancePop = Style.builder()
                    .type(StyleType.DANCE_POP)
                    .parent(pop)
                    .description("Dance-pop is a popular music subgenre that originated in the late 1970s to early 1980s.").build();

            Style rock = Style.builder()
                    .type(StyleType.ROCK)
                    .parent(null)
                    .description("Rock music is a broad genre of popular music that originated as rock and roll" +
                            " in the United States in the late 1940s and early 1950s")
                    .build();

            Style modernRock = Style.builder()
                    .type(StyleType.MODERN_ROCK)
                    .parent(rock)
                    .description("Modern rock is an umbrella term used to describe rock music that is found on college rock radio stations")
                    .build();

            Style hiphop = Style.builder()
                    .type(StyleType.HIPHOP)
                    .parent(null)
                    .description("Hip hop music is a genre of popular music that originated in the Bronx borough " +
                            "of New York City in the early 1970s.")
                    .build();

            Style melodicRap = Style.builder()
                    .type(StyleType.MELODIC_RAP)
                    .parent(hiphop)
                    .description("In the 1970s, the genre was born and has since spawned many subgenres, " +
                            "including gangsta rap, mumble rap, and SoundCloud rap")
                    .build();

            styleRepository.saveAllAndFlush(List.of(pop, dancePop, rock, modernRock,
                    hiphop, melodicRap));
        }
    }
}
