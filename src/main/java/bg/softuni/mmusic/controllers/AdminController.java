package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.ModifyRolesDto;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import bg.softuni.mmusic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public AdminController(UserService userService, UserRoleRepository userRoleRepository) {
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
    }

    @Secured(Authorities.ADMIN)
    @GetMapping
    public ModelAndView changeUserRole(ModelAndView modelAndView) {

        modelAndView.setViewName("admin");
        List<UserRole> allRoles = userRoleRepository.findAll();
        List<User> allUsers = userService.getAllUsers();

        modelAndView.addObject("allRoles", allRoles);
        modelAndView.addObject("users", allUsers);

        return modelAndView;
    }
    @Secured(Authorities.ADMIN)
    @PutMapping("/add/{uuid}")
    public String AddRole(@PathVariable(name = "uuid") String uuid, ModifyRolesDto modifyRolesDto) {
        userService.modifyRoles(uuid, modifyRolesDto);
        return "redirect:/admin";
    }
}
