package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.StyleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StyleService {
    private final StyleRepository styleRepository;

    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public Optional<Style> findByStyle(StyleType type) {
        return styleRepository.findByType(type);
    }
}
