package bg.softuni.mmusic.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInit implements CommandLineRunner {
    private final RoleSeeder roleSeeder;
    private final StyleSeeder styleSeeder;
    private final UserSeeder userSeeder;
    private final SongSeeder songSeeder;

    public DbInit(RoleSeeder roleSeeder, StyleSeeder styleSeeder, UserSeeder userSeeder, SongSeeder songSeeder) {
        this.roleSeeder = roleSeeder;
        this.styleSeeder = styleSeeder;
        this.userSeeder = userSeeder;
        this.songSeeder = songSeeder;
    }

    @Override
    public void run(String... args) throws Exception {
        styleSeeder.init();
        roleSeeder.init();
        userSeeder.init();
        songSeeder.init();

    }

}
