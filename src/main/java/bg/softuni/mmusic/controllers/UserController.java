package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.user.ChangePasswordDto;
import bg.softuni.mmusic.model.dtos.user.UserProfileDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.song.FavouriteSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

        return "auth-user-profile";
    }

    @GetMapping("/{uuid}/profile")
    public String getUserProfile(@PathVariable(name = "uuid") String uuid, Model model) {
        User user = userService.getUserByUuid(uuid);
        UserProfileDto userProfileInfo = userService.getUserProfileInfo(user.getUuid());

        List<PublicSimpleSongDto> userSongs = userService.getUserPublicSongs(user);
        List<PublicSimplePlaylistDto> userPlaylists = userService.getUserPublicPlaylists(user);

        model.addAttribute("user", userProfileInfo);
        model.addAttribute("songs", userSongs);
        model.addAttribute("playlists", userPlaylists);

        return "user-profile";
    }

    /**
     * { @code PUT / update }: user can update its fields (specified in the request as params)
     * @return redirect to user-profile
     * */
    @PutMapping("/{uuid}/update")
    public String update(@PathVariable(name = "uuid") String uuid,
                         @Valid UserProfileDto userProfileDto) {

        User userToUpdate = userService.getUserByUuid(uuid);
        userService.update(userToUpdate, userProfileDto);

        return "redirect:/user/profile";
    }

    /**
     * { @code PUT / change password }: user can c change its password
     * @return redirect to user-profile
     * */
    @PutMapping("/change-password")
    public String changePassword(@Valid ChangePasswordDto validation,
                                 BindingResult bindingResult) {

        User authUser = authService.getAuthenticatedUser();
        if (!validation.getNewPassword().equals(validation.getReEnterPassword())) {
            bindingResult.addError(new FieldError("differentPasswords",
                    "reEnterPassword", "Passwords does not match!"));
        }

        userService.changePassword(validation.getOldPassword(), validation.getNewPassword(), authUser);
        return "redirect:/user/profile";
    }
}
