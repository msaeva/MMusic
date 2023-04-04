package bg.softuni.mmusic.controllers.error;

import bg.softuni.mmusic.model.error.SongNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class InvalidSongException {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(SongNotFoundException.class)
    public ModelAndView onSongNotFound(SongNotFoundException snfe) {

        ModelAndView modelAndView = new ModelAndView("song-error");
        modelAndView.addObject("message", snfe.getMessage());

        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(bg.softuni.mmusic.model.error.InvalidSongException.class)
    public ModelAndView onInvalidSong(bg.softuni.mmusic.model.error.InvalidSongException ise) {

        ModelAndView modelAndView = new ModelAndView("song-error");
        modelAndView.addObject("message", ise.getMessage());
        return modelAndView;
    }
}
