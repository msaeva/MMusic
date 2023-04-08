package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.enums.StyleType;
import bg.softuni.mmusic.model.error.InvalidUserException;
import bg.softuni.mmusic.repositories.PictureRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.StyleRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SongSeeder {
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final PictureRepository pictureRepository;

    public SongSeeder(SongRepository songRepository, UserRepository userRepository, StyleRepository styleRepository, PictureRepository pictureRepository) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.styleRepository = styleRepository;
        this.pictureRepository = pictureRepository;
    }

    public void init() {
        if (songRepository.count() == 0) {

            User eminem = userRepository.findByUsername("eminem")
                    .orElseThrow(() -> new InvalidUserException("User with username eminem not found!"));

            User rihanna = userRepository.findByUsername("rihanna")
                    .orElseThrow(() -> new InvalidUserException("User with username rihanna not found!"));

            User madonna = userRepository.findByUsername("madonna")
                    .orElseThrow(() -> new InvalidUserException("User with username madonna not found!"));

            User tinaTurner = userRepository.findByUsername("tinaturner")
                    .orElseThrow(() -> new InvalidUserException("User with username tinaturner not found!"));
            User pink = userRepository.findByUsername("pink_")
                    .orElseThrow(() -> new InvalidUserException("User with username pink_ not found!"));

            User beyonce = userRepository.findByUsername("beyonce")
                    .orElseThrow(() -> new InvalidUserException("User with username beyonce not found!"));

            Style pop = styleRepository.findByType(StyleType.POP).orElseThrow(NoSuchElementException::new);
            Style rock = styleRepository.findByType(StyleType.ROCK).orElseThrow(NoSuchElementException::new);
            Style hiphop = styleRepository.findByType(StyleType.HIPHOP).orElseThrow(NoSuchElementException::new);
            Style dancePop = styleRepository.findByType(StyleType.DANCE_POP).orElseThrow(NoSuchElementException::new);
            Style melodicRap = styleRepository.findByType(StyleType.MELODIC_RAP).orElseThrow(NoSuchElementException::new);
            Style modernRock = styleRepository.findByType(StyleType.MODERN_ROCK).orElseThrow(NoSuchElementException::new);

            Picture picture = pictureRepository.findByTitle("picture").orElseThrow(NoSuchElementException::new);
            Picture picture2 = pictureRepository.findByTitle("picture2").orElseThrow(NoSuchElementException::new);
            Picture picture3 = pictureRepository.findByTitle("picture3").orElseThrow(NoSuchElementException::new);
            Picture picture4 = pictureRepository.findByTitle("picture4").orElseThrow(NoSuchElementException::new);
            Picture picture5 = pictureRepository.findByTitle("picture5").orElseThrow(NoSuchElementException::new);
            Picture picture6 = pictureRepository.findByTitle("picture6").orElseThrow(NoSuchElementException::new);
            Picture picture7 = pictureRepository.findByTitle("picture7").orElseThrow(NoSuchElementException::new);
            Picture picture8 = pictureRepository.findByTitle("picture8").orElseThrow(NoSuchElementException::new);
            Picture picture9 = pictureRepository.findByTitle("picture9").orElseThrow(NoSuchElementException::new);

            Song song = Song.builder()
                    .title("Lose Yourself")
                    .description("\"Lose Yourself\" was a commercial success, becoming Eminem's first Billboard Hot 100 number-one single and remaining there for twelve consecutive weeks.")
                    .status(SongStatus.PUBLIC)
                    .picture(picture)
                    .Style(hiphop)
                    .author(eminem)
                    .createdDate(LocalDate.now())
                    .videoUrl("_Yhyp-_hX2s")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song2 = Song.builder()
                    .title("Godzilla")
                    .description("\"Godzilla\" is a song by American rapper Eminem featuring fellow American rapper and singer Juice Wrld, released as a single on January 28, 2020")
                    .status(SongStatus.PUBLIC)
                    .picture(picture2)
                    .Style(hiphop)
                    .author(eminem)
                    .createdDate(LocalDate.now())
                    .videoUrl("r_0JjYUe5jo")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song3 = Song.builder()
                    .title("Russian Roulette")
                    .description("\"Russian Roulette\" is a pop and R&B ballad that contains dark, morbid, and tense atmospheric elements in its composition")
                    .status(SongStatus.PUBLIC)
                    .picture(picture3)
                    .Style(pop)
                    .author(rihanna)
                    .createdDate(LocalDate.now())
                    .videoUrl("ZQ2nCGawrSY")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song4 = Song.builder()
                    .title("Stay")
                    .description("The song's lyrics speak of temptation and the inability to resist true love. Music critics were generally positive in their opinion regarding the balladry and most described it as a standout track on the album.")
                    .status(SongStatus.PUBLIC)
                    .picture(picture4)
                    .Style(pop)
                    .author(rihanna)
                    .createdDate(LocalDate.now())
                    .videoUrl("JF8BRvqGCNs")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song5 = Song.builder()
                    .title("Like A Player")
                    .description("\"Like a Prayer\" is a song by American singer Madonna from her 1989 fourth studio album of the same name")
                    .status(SongStatus.PUBLIC)
                    .picture(picture5)
                    .Style(dancePop)
                    .author(madonna)
                    .createdDate(LocalDate.now())
                    .videoUrl("79fzeNUqQbQ")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song6 = Song.builder()
                    .title("The Best")
                    .description("In 1989, American singer Tina Turner released a cover version of \"The Best\" for her seventh studio album, Foreign Affair. ")
                    .status(SongStatus.PUBLIC)
                    .picture(picture6)
                    .Style(rock)
                    .author(tinaTurner)
                    .createdDate(LocalDate.now())
                    .videoUrl("GC5E8ie2pdM")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song7 = Song.builder()
                    .title("Trustfall")
                    .description("Trustfall is the ninth studio album by American singer Pink. The album was released on February 17, 2023, through RCA Records.")
                    .status(SongStatus.PUBLIC)
                    .picture(picture7)
                    .Style(dancePop)
                    .author(pink)
                    .createdDate(LocalDate.now())
                    .videoUrl("D2KE2a5qo0g")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song song8 = Song.builder()
                    .title("Crazy In Love")
                    .description("\"Crazy in Love\" is a song recorded by American singer Beyoncé, featuring a rap verse and ad-libs from her future husband Jay-Z from her debut solo studio album Dangerously in Love (2003).")
                    .status(SongStatus.PUBLIC)
                    .picture(picture8)
                    .Style(pop)
                    .author(beyonce)
                    .createdDate(LocalDate.now())
                    .videoUrl("ViwtNLUqkMY")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            Song son9 = Song.builder()
                    .title("Daddy Issues")
                    .description("A sad but great song no doubt. The meaning is pretty straightforward." +
                            "This song is about two destructive people in a destructive relationship through no fault of their own. ")
                    .status(SongStatus.PUBLIC)
                    .picture(picture9)
                    .Style(modernRock)
                    .author(pink)
                    .createdDate(LocalDate.now())
                    .videoUrl("_Yhyp-_hX2s")
                    .likes(0)
                    .favouriteCount(0)
                    .build();

            songRepository.saveAllAndFlush(List.of(song, song2, song3, song4, song5, song6, song7, song8, son9));
        }
    }
}
