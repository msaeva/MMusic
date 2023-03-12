package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.ProfileDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.ProfileDetailedSongDto;
import bg.softuni.mmusic.model.dtos.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(ModelAndView modelAndView) {
        User authUser = authService.getAuthenticatedUser();
        List<ProfileDetailedSongDto> userSongs = userService.getUserSongs(authUser);
        List<ProfileDetailedPlaylistDto> userPlaylists = userService.getUserPlaylist(authUser);

        modelAndView.setViewName("user-profile");
        modelAndView.addObject("songs", userSongs);
        modelAndView.addObject("playlists", userPlaylists);

        return modelAndView;
    }

    @GetMapping("/{uuid}/profile")
    public ModelAndView getUserProfile(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        User user = userService.getUserByUuid(uuid);

        List<PublicSimpleSongDto> userSongs = userService.getUserPublicSongs(user);
        List<PublicSimplePlaylistDto> userPlaylists = userService.getUserPlaylists(user);

        modelAndView.setViewName("user-profile");
        modelAndView.addObject("songs", userSongs);
        modelAndView.addObject("playlists", userPlaylists);

        //TODO create view
        return modelAndView;

    }

    @GetMapping("/{uuid}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable(name = "uuid") String uuid) {

        return ResponseEntity.ok(userService.getUserByUuid(uuid));

    }


}
