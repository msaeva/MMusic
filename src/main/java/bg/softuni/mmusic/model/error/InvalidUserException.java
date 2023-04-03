package bg.softuni.mmusic.model.error;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message) {
        super(message);
    }
}
