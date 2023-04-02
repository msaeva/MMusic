package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.UserProfileDto;
import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Имплементацията на UserMapper ще бъде изложена като Bean
public interface UserMapper {

    User userRegisterDtoToUser(UserRegisterDto userRegisterDto);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserProfileDto toUserProfileDetailsDto(User user);
}
