package pl.budowniczowie.appbackend.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.budowniczowie.appbackend.auth.dto.AuthReq;
import pl.budowniczowie.appbackend.auth.dto.AuthRes;
import pl.budowniczowie.appbackend.auth.dto.RegisterReq;
import pl.budowniczowie.appbackend.auth.dto.UserRes;
import pl.budowniczowie.appbackend.mail.services.MailSenderService;
import pl.budowniczowie.appbackend.security.JwtService;
import pl.budowniczowie.appbackend.user.User;
import pl.budowniczowie.appbackend.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final MailSenderService mailSenderService;

    public ResponseEntity<?> authenticate( @NotNull AuthReq authReq, HttpServletResponse response) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtService.generateToken(user);
                final Cookie cookie = new Cookie("Authorization", token);
                cookie.setMaxAge(60 * 60 * 24 * 7); // 7 days


                response.addCookie(cookie);
            return ResponseEntity.ok().body(AuthRes.builder()
                            .token(token)
                            .user(
                                    UserRes.builder()
                                            .id(user.getId())
                                            .firstname(user.getFirstname())
                                            .lastname(user.getLastname())
                                            .email(user.getEmail())
                                            .birthDate(user.getBirthDate())
                                            .phoneNumber(user.getPhoneNumber())
                                            .street(user.getStreet())
                                            .houseNumber(user.getHouseNumber())
                                            .zipCode(user.getZipCode())
                                            .postOffice(user.getPostOffice())
                                            .town(user.getTown())
                                            .role(user.getRole())
                                            .build()
                            )
                    .build());

        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<?> register(@NotNull RegisterReq registerReq) {

        try {
            Optional<User> userExist = repository.findByEmail(registerReq.getEmail());
            if(userExist.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email address " +registerReq.getEmail() + " already exists!");
            }
            User user = User.builder()
                    .firstname(registerReq.getFirstname())
                    .lastname(registerReq.getLastname())
                    .email(registerReq.getEmail())
                    .birthDate(registerReq.getBirthDate())
                    .phoneNumber(registerReq.getPhoneNumber())
                    .street(registerReq.getStreet())
                    .houseNumber(registerReq.getHouseNumber())
                    .zipCode(registerReq.getZipCode())
                    .postOffice(registerReq.getPostOffice())
                    .town(registerReq.getTown())
                    .isAccountNonExpired(true)
                    .password(passwordEncoder.encode(registerReq.getPassword()))
                    .role(registerReq.getRole())
                    .build();
            repository.save(user);
//TODO: uncomment this when you have mail server configured
//                mailSenderService.sendEmail("Michal <poczta@onet.pl>", "Test e-mail", "Testing email functionality");

            return ResponseEntity.ok().body("User registered successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }

    }
}
