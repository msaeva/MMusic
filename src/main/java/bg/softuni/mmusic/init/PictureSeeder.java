package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.repositories.PictureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureSeeder {
    private final PictureRepository pictureRepository;

    public PictureSeeder(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public void init() {
        if (pictureRepository.count() == 0) {
            Picture picture = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680679626/6077b761-7c13-4a59-93a4-e7a07a8e6d00.jpg")
                    .title("picture")
                    .build();

            Picture picture2 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947214/b52f7fa5-715b-4f4c-b9b3-47d6090c039d.jpg")
                    .title("picture2")
                    .build();

            Picture picture3 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680679268/f332560e-85b4-4bed-b1d4-dd66f6bd0889.png")
                    .title("picture3")
                    .build();

            Picture picture4 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947190/e5969259-bc23-4a49-97e3-b00ed8aa91ad.png")
                    .title("picture4")
                    .build();

            Picture picture5 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947167/60f8e449-c7a8-4260-ab90-9de8138cd3d8.jpg")
                    .title("picture5")
                    .build();

            Picture picture6 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947147/5915228a-9624-48e3-8f57-2702879df68d.jpg")
                    .title("picture6")
                    .build();

            Picture picture7 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947135/9c78fa12-6f32-4809-be9d-1d89030a7fd2.jpg")
                    .title("picture7")
                    .build();

            Picture picture8 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680947135/9c78fa12-6f32-4809-be9d-1d89030a7fd2.jpg")
                    .title("picture8")
                    .build();

            Picture picture9 = Picture.builder()
                    .url("https://res.cloudinary.com/dfmavylku/image/upload/v1680941305/25699a2a-cb75-4a32-be4f-dbe830f016bb.jpg")
                    .title("picture9")
                    .build();

            pictureRepository.saveAllAndFlush(List.of(picture, picture2,
                    picture3, picture4, picture5, picture6, picture7, picture8, picture9));
        }
    }
}
