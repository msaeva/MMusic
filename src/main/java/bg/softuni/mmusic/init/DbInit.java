package bg.softuni.mmusic.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {
    private final RoleSeeder roleSeeder;
    private final StyleSeeder styleSeeder;
    private final UserSeeder userSeeder;
    private final SongSeeder songSeeder;
    private final PlaylistSeeder playlistSeeder;
    private final PictureSeeder pictureSeeder;

    public DbInit(RoleSeeder roleSeeder, StyleSeeder styleSeeder, UserSeeder userSeeder, SongSeeder songSeeder, PlaylistSeeder playlistSeeder, PictureSeeder pictureSeeder) {
        this.roleSeeder = roleSeeder;
        this.styleSeeder = styleSeeder;
        this.userSeeder = userSeeder;
        this.songSeeder = songSeeder;
        this.playlistSeeder = playlistSeeder;
        this.pictureSeeder = pictureSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        styleSeeder.init();
        roleSeeder.init();
        pictureSeeder.init();
        userSeeder.init();
        songSeeder.init();
        playlistSeeder.init();
    }

}
