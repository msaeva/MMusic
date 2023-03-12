package bg.softuni.mmusic.controllers.error;

import bg.softuni.mmusic.model.error.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class InvalidUserException {
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView onProductNotFound(UserNotFoundException onfe) {

        ModelAndView modelAndView = new ModelAndView("user-not-found");
        modelAndView.addObject("userId", onfe.getUserId());

        return modelAndView;
    }

}
