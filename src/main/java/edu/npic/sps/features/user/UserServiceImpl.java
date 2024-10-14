package edu.npic.sps.features.user;

import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.user.dto.*;
import edu.npic.sps.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void deleteByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found")
        );

        user.setIsDeleted(true);

        userRepository.save(user);
    }

    @Override
    public void updateUser(String uuid, UpdateUserRequest updateUserRequest) {

        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found")
        );

        // Convert role names to uppercase and find corresponding Role entities
        List<Role> roles = updateUserRequest.roleName().stream()
                .map(String::toUpperCase)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")))
                .collect(Collectors.toList());

        user.setRoles(roles);

        userMapper.fromUpdateUserRequest(updateUserRequest,user);

        userRepository.save(user);
    }

    @Override
    public void createUser(CreateUser createUser){

        if ( userRepository.existsByEmail(createUser.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (userRepository.existsByPhoneNumber(createUser.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists");
        }

        User user = userMapper.fromCreateUser(createUser);

        // Convert role names to uppercase and find corresponding Role entities
        List<Role> roles = createUser.roleName().stream()
                .map(String::toUpperCase)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")))
                .collect(Collectors.toList());


        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(createUser.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsVerified(true);
        user.setIsDeleted(false);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        userRepository.save(user);

    }

    @Override
    public List<UserDetailResponse> findAll() {

        List<User> users = userRepository.findAllNotDeleted();

        return  users.stream().map(user -> UserDetailResponse.builder()
                .uuid(user.getUuid())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .phoneNumber(user.getPhoneNumber())
                .isVerified(user.getIsVerified())
                .roleNames(user.getRoles().stream().map(Role::getName).toList())
                .build()).toList();
    }

    @Override
    public void register(CreateUserRegister createUserRegister) {

        if (userRepository.existsByEmail(createUserRegister.email())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists!"
            );
        }

        if (!createUserRegister.password().equals(createUserRegister.confirmPassword())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Passwords and confirm passwords do not match!"
            );
        }

        User user = userMapper.fromCreateUserRegister(createUserRegister);
        user.setUuid(UUID.randomUUID().toString());
        user.setIsVerified(false);
        user.setPassword(passwordEncoder.encode(createUserRegister.password()));
        user.setProfileImage("default-avatar.png");
        user.setCreatedAt(LocalDateTime.now());
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsDeleted(false);

        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("STAFF").build());
        user.setRoles(roles);
        userRepository.save(user);
    }

}
