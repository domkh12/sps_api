package edu.npic.sps.features.auth;

import edu.npic.sps.features.auth.dto.*;
import edu.npic.sps.features.user.dto.CreateUserRegister;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<Void> logout(HttpServletResponse response);

    ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response);

    void register(CreateUserRegister createUserRegister) throws MessagingException;

    void verify(VerifyRequest verifyRequest);
}
