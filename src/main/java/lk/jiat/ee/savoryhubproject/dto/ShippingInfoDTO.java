package lk.jiat.ee.savoryhubproject.dto;
import lombok.Data;

@Data
public class ShippingInfoDTO {
    private String fullName;
    private String address;
    private String city;
    private String postalCode;
    private String phone;
}