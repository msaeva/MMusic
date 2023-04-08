package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.ModifyRolesDto;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import bg.softuni.mmusic.services.UserRoleService;
import bg.softuni.mmusic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public AdminController(UserService userService,
                           UserRoleRepository userRoleRepository,
                           UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Secured(Authorities.ADMIN)
    @GetMapping
    public ModelAndView getAllUsers(ModelAndView modelAndView) {

        modelAndView.setViewName("admin");

        modelAndView.addObject("allRoles", userRoleService.findAll());
        modelAndView.addObject("users", userService.getAllUsers());

        return modelAndView;
    }

    @Secured(Authorities.ADMIN)
    @PutMapping("/add/{uuid}")
    public String changeRole(@PathVariable(name = "uuid") String uuid, ModifyRolesDto modifyRolesDto) {
        userService.modifyRoles(uuid, modifyRolesDto);
        return "redirect:/admin";
    }
}
