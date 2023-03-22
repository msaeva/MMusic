package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.UserProfileDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.song.FavouriteSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(AuthService authService, UserService userService, UserMapper userMapper) {
        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        User authUser = authService.getAuthenticatedUser();
        UserProfileDto userProfileInfo = userService.getUserProfileInfo(authUser.getUuid());

        List<PublicSimpleSongDto> userSongs = userService.getUserSongs(authUser);
        List<PublicSimplePlaylistDto> userPlaylists = userService.getUserPlaylist(authUser);
        List<FavouriteSongDto> favouriteSongs = userService.getFavouriteSongs(authUser);

        model.addAttribute("user", userProfileInfo);
        model.addAttribute("ownSongs", userSongs);
        model.addAttribute("ownPlaylists", userPlaylists);
        model.addAttribute("favouriteSongs", favouriteSongs);

        return "user-profile";
    }


    @PutMapping("/{uuid}/update")
    public String update(@PathVariable(name = "uuid") String uuid,
                         @Valid UserProfileDto userProfileDto) {

        User userToUpdate = userService.getUserByUuid(uuid);
        userService.update(userToUpdate, userProfileDto);

        return "redirect:/user/profile";
    }


    @GetMapping("/{uuid}/profile")
    public String getUserProfile(@PathVariable(name = "uuid") String uuid, Model model) {
        User user = userService.getUserByUuid(uuid);

        List<PublicSimpleSongDto> userSongs = userService.getUserPublicSongs(user);
        List<PublicSimplePlaylistDto> userPlaylists = userService.getUserPlaylists(user);

        model.addAttribute("songs", userSongs);
        model.addAttribute("playlists", userPlaylists);

        return "user-profile";

    }

}
