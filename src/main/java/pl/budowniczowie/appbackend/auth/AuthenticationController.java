package pl.budowniczowie.appbackend.auth;

import com.auth0.jwt.interfaces.Header;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.budowniczowie.appbackend.auth.dto.AuthReq;
import pl.budowniczowie.appbackend.auth.dto.RegisterReq;
import pl.budowniczowie.appbackend.upload.FileStorageServiceImpl;
import pl.budowniczowie.appbackend.upload.ResponseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final FileStorageServiceImpl storageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@NotNull @RequestBody RegisterReq registerReq, HttpServletResponse response) {
       return authenticationService.register(registerReq);

    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@NotNull @RequestBody AuthReq authReq, HttpServletResponse response ) {
    return authenticationService.authenticate(authReq, response);
    }
    @PostMapping("/add_avatar")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @RequestBody RegisterReq text) {
        System.out.println(text);
        String message = "";
        try {
            storageService.setRoot("uploads/avatars");
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
