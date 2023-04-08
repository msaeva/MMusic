package bg.softuni.mmusic.services;

import bg.softuni.mmusic.repositories.UserFavouriteSongsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavouriteSongsService {
    private final UserFavouriteSongsRepository userFavouriteSongsRepository;

    public UserFavouriteSongsService(UserFavouriteSongsRepository userFavouriteSongsRepository) {
        this.userFavouriteSongsRepository = userFavouriteSongsRepository;
    }

    public List<String> getUserFavouriteSongs(String uuid) {
        return userFavouriteSongsRepository.getUserFavouriteSongs(uuid);
    }
}
