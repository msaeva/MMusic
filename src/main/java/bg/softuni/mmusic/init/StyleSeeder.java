package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StyleSeeder implements CommandLineRunner {

    private final StyleRepository styleRepository;

    public StyleSeeder(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (styleRepository.count() == 0){
            for(StyleType type : StyleType.values()){
                styleRepository.saveAndFlush(new Style("description", type));
            }
        }
    }
}
