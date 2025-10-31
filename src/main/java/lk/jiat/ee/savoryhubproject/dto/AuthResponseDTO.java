package lk.jiat.ee.savoryhubproject.dto;

import lk.jiat.ee.savoryhubproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private User user;
}