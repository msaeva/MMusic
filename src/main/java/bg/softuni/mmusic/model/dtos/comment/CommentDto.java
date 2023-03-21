package bg.softuni.mmusic.model.dtos.comment;

public class CommentDto {
    private String uuid;
    private String text;
    private String authorName;
    private String createdDate;

    public CommentDto() {
    }

    public String getUuid() {
        return uuid;
    }

    public CommentDto setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentDto setText(String text) {
        this.text = text;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public CommentDto setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public CommentDto setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
        return this;
    }
}
