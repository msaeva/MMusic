package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.UserRegisterDto;
import bg.softuni.mmusic.model.entities.User;
import jakarta.persistence.SqlResultSetMappings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Имплементацията на UserMapper ще бъде изложена като Bean
public interface UserMapper {

    User userRegisterDtoToUser(UserRegisterDto userRegisterDto);
}
