package pl.budowniczowie.appbackend.user;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        return ResponseEntity.ok("user");
}

    @GetMapping("/admin")
    public ResponseEntity<?> getAdmin() {
        return ResponseEntity.ok("admin");
    }
}