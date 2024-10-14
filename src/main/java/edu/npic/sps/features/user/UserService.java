package edu.npic.sps.features.user;

import edu.npic.sps.features.user.dto.*;

import java.util.List;

public interface UserService {

    void deleteByUuid(String uuid);

    void updateUser(String uuid, UpdateUserRequest updateUserRequest);

    void createUser(CreateUser createUser);

    List<UserDetailResponse> findAll();

    void register(CreateUserRegister createUserRegister);
}
