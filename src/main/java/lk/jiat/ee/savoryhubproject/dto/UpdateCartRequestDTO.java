package lk.jiat.ee.savoryhubproject.dto;
import lombok.Data;

@Data
public class UpdateCartRequestDTO {
    private Long cartItemId;
    private int quantity;
}