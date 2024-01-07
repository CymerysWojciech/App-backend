package pl.budowniczowie.appbackend.auth.dto;

import lombok.Builder;
import lombok.Data;
import pl.budowniczowie.appbackend.user.Role;

import java.time.LocalDate;
import java.util.List;

@Builder
public record UserRes(
        Long id,
         String firstname,
         String lastname,
         String email,

         List<Role>role ,
         LocalDate birthDate,
         String town,
         String postOffice,
         String zipCode,
         String street,
         String houseNumber,
         String phoneNumber
) {
}
