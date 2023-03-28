package bg.softuni.mmusic.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Size(min = 4, max = 12)
    @Column(unique = true, nullable = false)
    private String username;

    @Size(min = 2, max = 15)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(min = 2, max = 15)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;


    @Size(min = 5)
    @Column
    private String password;

    @Transient
    @Size(min = 5)
    private String confirmPassword;

    @Size(min = 5, max = 300)
    @Column()
    private String about;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Song> ownSongs;

    @ManyToMany
    private Set<Song> downloadSongs;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Song> favouriteSongs;

    @ManyToMany
    private Set<Song> likedSongs;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Playlist> playlists;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmedPassword='" + confirmPassword + '\'' +
                ", createdDate=" + createdDate +
                ", role=" + roles +
                ", downloadSongs=" + downloadSongs +
                ", favouriteSongs=" + favouriteSongs +
                ", playlists=" + playlists +
                '}';
    }

    public User(String username, String firstName, String lastName, String email, String password, LocalDate createdDate, Set<UserRole> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.roles = roles;
    }
}
