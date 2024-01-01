package pl.budowniczowie.appbackend.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.budowniczowie.appbackend.auth.dto.AuthReq;
import pl.budowniczowie.appbackend.auth.dto.RegisterReq;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@NotNull @RequestBody RegisterReq registerReq) {
       return authenticationService.register(registerReq);

    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@NotNull @RequestBody AuthReq authReq, HttpServletResponse response ) {
    return authenticationService.authenticate(authReq, response);
    }

}
