package bg.softuni.mmusic.model.error;

public class SongNotFoundException extends RuntimeException {

    private final String songId;

    public SongNotFoundException(String objectId) {
        super("Sorry, song with id: " + objectId + " not found!");
        this.songId = objectId;

    }

    public String getSongId() {
        return songId;
    }
}
