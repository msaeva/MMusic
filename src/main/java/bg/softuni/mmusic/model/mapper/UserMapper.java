package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.UserProfileDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // Имплементацията на UserMapper ще бъде изложена като Bean
public interface UserMapper {

    User userRegisterDtoToUser(UserRegisterDto userRegisterDto);

    UserProfileDto toUserProfileDetailsDto(User user);
}
