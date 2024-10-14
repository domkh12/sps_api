package edu.npic.sps.mapper;

import edu.npic.sps.domain.User;
import edu.npic.sps.features.user.dto.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateUserRequest(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    List<UserDetailResponse> toUserDetailResponse(List<User> users);

    User fromCreateUserRegister(CreateUserRegister createUserRegister);

    User fromCreateUser(CreateUser createUser);

}
