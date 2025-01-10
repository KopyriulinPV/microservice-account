package microservice_account.microservice_account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataDto {

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String statusCode;
    private String birthDate;
    private String messagePermission;
    private String lastOnlineTime;
    private String emojiStatus;
    private Boolean deleted;
    private Boolean blocked;
    private Boolean isOnline;

}
