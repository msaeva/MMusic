package bg.softuni.mmusic.model.error;

public class UserNotFoundException extends RuntimeException {
    private final String userId;

    public UserNotFoundException(String objectId) {
        super("User with ID: " + objectId + " not found!");
        this.userId = objectId;
    }

    public String getUserId() {
        return userId;
    }
}
