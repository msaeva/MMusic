package bg.softuni.mmusic.model.error;

public class InvalidSongException extends RuntimeException {
    private final String songId;

    public InvalidSongException(String message, String songId) {
       super(message);
        this.songId = songId;
    }

    public String getSongId() {
        return songId;
    }
}
