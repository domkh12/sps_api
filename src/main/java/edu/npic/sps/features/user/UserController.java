package edu.npic.sps.features.user;

import edu.npic.sps.features.user.dto.CreateUser;
import edu.npic.sps.features.user.dto.CreateUserRegister;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import edu.npic.sps.features.user.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteByUuid(@PathVariable String uuid){
        userService.deleteByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PatchMapping("/{uuid}")
    void updateUser(@PathVariable String uuid, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateUser(uuid, updateUserRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    void createUser( @Valid @RequestBody CreateUser createUser) {
        userService.createUser(createUser);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registers")
    void register(@Valid @RequestBody CreateUserRegister createUserRegister){
        userService.register(createUserRegister);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    List<UserDetailResponse> findAll (){
        return userService.findAll();
    }

}
