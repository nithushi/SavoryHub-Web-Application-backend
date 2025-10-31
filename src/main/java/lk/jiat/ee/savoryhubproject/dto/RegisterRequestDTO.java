package lk.jiat.ee.savoryhubproject.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "First Name Can Not Be Empty")
    private String fname;

    @NotBlank(message = "Last Name Can Not Be Empty")
    private String lname;

    @NotBlank(message = "Email Can Not Be Empty")
    @Email(message = "Email Should Be Valid")
    private String email;

    @NotBlank(message = "Password Can Not Be Empty")
    private String password;

    @NotBlank(message = "Contact Can Not Be Empty")
    private String contact;

}
