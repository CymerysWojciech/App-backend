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

    public ResponseEntity<?> authenticate( @NotNull AuthReq authReq, HttpServletResponse response) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.getEmail(), authReq.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtService.generateToken(user);
                final Cookie cookie = new Cookie("Authorization", token);
                response.addCookie(cookie);
            return ResponseEntity.ok().body(AuthRes.builder()
                            .token(token)
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
                    .password(passwordEncoder.encode(registerReq.getPassword()))
                    .role(registerReq.getRole())
                    .build();
            repository.save(user);
            jwtService.generateToken(user);
            return ResponseEntity.ok().body("User registered successfully!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }

    }
}
