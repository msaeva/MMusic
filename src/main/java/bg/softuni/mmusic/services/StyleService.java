package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.repositories.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StyleService {
    private final StyleRepository styleRepository;
    @Autowired
    public StyleService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public Style findByStyle(StyleType type) {
        return styleRepository.findByType(type).orElseThrow(NoSuchElementException::new);

    }

    public Style findByUuid(String uuid) {
        return styleRepository.findByUuid(uuid).orElseThrow(NoSuchElementException::new);
    }
}
