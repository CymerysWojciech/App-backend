package pl.budowniczowie.appbackend.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import pl.budowniczowie.appbackend.user.Role;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterReq {
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    @NonNull
    public String password;
    @NonNull
    public String email;
    @NonNull
    public List<Role> role;
    private String pathAvatar;


}
