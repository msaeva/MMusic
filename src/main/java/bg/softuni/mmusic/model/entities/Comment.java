package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    private LocalDateTime created;

    @Column(nullable = false)
    private String text;

    @ManyToOne
    private User author;

    @ManyToOne
    private Song song;


    public LocalDateTime getCreated() {
        return created;
    }

    public Comment setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getText() {
        return text;
    }

    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public User getAuthor() {
        return author;
    }

    public Comment setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Song getSong() {
        return song;
    }

    public Comment setSong(Song song) {
        this.song = song;
        return this;
    }

}
