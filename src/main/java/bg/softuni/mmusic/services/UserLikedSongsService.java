package bg.softuni.mmusic.services;

import bg.softuni.mmusic.repositories.UserLikedSongsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLikedSongsService {
    private final UserLikedSongsRepository userLikedSongsRepository;

    public UserLikedSongsService(UserLikedSongsRepository userLikedSongsRepository) {
        this.userLikedSongsRepository = userLikedSongsRepository;
    }

    public List<String> getUserLikedSongs(String uuid) {
       return userLikedSongsRepository.getUserLikedSongs(uuid);
    }
}
