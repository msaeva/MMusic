package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.ProfileDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.UserProfileDto;
import bg.softuni.mmusic.model.dtos.song.FavouriteSongDto;
import bg.softuni.mmusic.model.dtos.song.ProfileDetailedSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

        List<ProfileDetailedSongDto> userSongs = userService.getUserSongs(authUser);
        List<ProfileDetailedPlaylistDto> userPlaylists = userService.getUserPlaylist(authUser);
        List<FavouriteSongDto> favouriteSongs = userService.getFavouriteSongs(authUser);

        model.addAttribute("user", userProfileInfo);
        model.addAttribute("songs", userSongs);
        model.addAttribute("playlists", userPlaylists);
        model.addAttribute("favouriteSongs", favouriteSongs);

        return "user-profile";
    }

    @PutMapping("/{uuid}/update")
    public ModelAndView update(@PathVariable(name = "uuid") String uuid,
                              @Valid UserProfileDto userProfileDto,
                              ModelAndView modelAndView) {

        modelAndView.setViewName("redirect:/user/profile");

        User userToUpdate = userService.getUserByUuid(uuid);
        userService.update(userToUpdate, userProfileDto);

        return modelAndView;
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
