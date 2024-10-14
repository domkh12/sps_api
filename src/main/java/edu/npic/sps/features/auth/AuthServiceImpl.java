package edu.npic.sps.features.auth;

import edu.npic.sps.domain.EmailVerification;
import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.auth.dto.*;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.user.dto.CreateUserRegister;
import edu.npic.sps.mapper.UserMapper;
import edu.npic.sps.util.RandomOtp;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSenderImpl mailSender;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtEncoderRefreshToken;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Autowired
    @Qualifier("jwtEncoderRefreshToken")
    public void setJwtEnCoderRefreshToken(JwtEncoder jwtEnCoderRefreshToken){
        this.jwtEncoderRefreshToken = jwtEnCoderRefreshToken;
    }

    @Override
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        // Clear the refresh token cookie
        ResponseCookie clearRefreshTokenCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearRefreshTokenCookie.toString());

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        System.out.println(Arrays.toString(request.getCookies()));
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {

                if ("token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing");
        }

        log.info("Refresh token request: {}", refreshToken);
        Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
        auth = jwtAuthenticationProvider.authenticate(auth);
        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        log.info("log4");
        log.info("New Scope: {}", scope);
        log.info("Auth: {}", auth);

        Instant now = Instant.now();

        Jwt jwt = (Jwt) auth.getPrincipal();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs", "reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(15, ChronoUnit.SECONDS))
                .claim("scope", scope)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt = jwtEncoder.encode(jwtEncoderParameters);

        String accessToken = encodedJwt.getTokenValue();

        if (Duration.between(Instant.now(), jwt.getExpiresAt()).toDays() < 2) {
            // Create refresh token claims set
            JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .issuer("web")
                    .audience(List.of("nextjs", "reactjs"))
                    .subject("Refresh Token")
                    .expiresAt(now.plus(30, ChronoUnit.SECONDS))
                    .build();
            JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();

            // Set new refresh token as an httpOnly cookie
            ResponseCookie refreshTokenCookie = ResponseCookie.from("token", refreshToken)
                    .httpOnly(true)
                    .secure(true) // Use secure flag in production
                    .sameSite("None")
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 days
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        }

        return ResponseEntity.ok(JwtResponse.builder()
                .tokenType(TOKEN_TYPE)
                .accessToken(accessToken)
                .build());
    }

    @Override
    public void verify(VerifyRequest verifyRequest) {

        User user = userRepository.findByEmail(verifyRequest.email()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Invalid email"
                )
        );

        EmailVerification emailVerification = emailVerificationRepository.findByUser(user).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Verification failed"
                )
        );

        if (!emailVerification.getVerificationCode().equals(verifyRequest.verificationCode())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Verification Code invalid"
            );
        }

        if(LocalTime.now().isAfter(emailVerification.getExpiryTime())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Verification Expired"
            );
        };
        user.setIsDeleted(false);
        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<JwtResponse> login(LoginRequest loginRequest, HttpServletResponse response) {
        System.out.println("email:"+loginRequest.email());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        auth = daoAuthenticationProvider.authenticate(auth);

        // prepare SCOPE
        log.info("Authorities: {}", auth.getAuthorities());

        // ADMIN MANAGER USER

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        ;
        log.info("SCOPE : {}", scope);

        Instant now = Instant.now();

        // Create access token claims set
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs","reactjs"))
                .subject("Access Token")
                .expiresAt(now.plus(15, ChronoUnit.SECONDS))
                .claim("scope", scope)
                .build();

        // Create refresh token claims set
        JwtClaimsSet jwtClaimsSetRefreshToken = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("nextjs","reactjs"))
                .subject("Refresh Token")
                .expiresAt(now.plus(30, ChronoUnit.SECONDS))
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(jwtClaimsSet);
        Jwt jwt = jwtEncoder.encode(jwtEncoderParameters);

        JwtEncoderParameters jwtEncoderParametersRefreshToken = JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
        Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);

        String accessToken = jwt.getTokenValue();
        String refreshToken = jwtRefreshToken.getTokenValue();

        // Set refresh token as an httpOnly cookie
        ResponseCookie refreshTokenCookie = ResponseCookie.from("token", refreshToken)
                .httpOnly(true)
                .secure(true) // Use secure flag in production
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok(JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .build());
    }

    @Override
    public void register(CreateUserRegister createUserRegister) throws MessagingException {

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
        user.setPassword(passwordEncoder.encode(createUserRegister.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsVerified(false);
        user.setProfileImage("default-avatar.jpg");
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsDeleted(false);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().name("USER").build());
        user.setRoles(roles);

        userRepository.save(user);

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(user.getEmail());
        emailVerification.setExpiryTime(LocalTime.now().plusMinutes(3));
        emailVerification.setUser(user);
        emailVerification.setVerificationCode(RandomOtp.generateSecurityCode());
        emailVerificationRepository.save(emailVerification);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Email Verification - SPS");
        helper.setTo(user.getEmail());
        helper.setFrom(adminMail);
        helper.setText(emailVerification.getVerificationCode());

        mailSender.send(mimeMessage);
    }
}
