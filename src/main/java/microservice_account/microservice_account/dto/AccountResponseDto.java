package microservice_account.microservice_account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private String id;

//    @NotBlank
    private String firstName;

//    @NotBlank
    private String lastName;

//    @Email
    private String email;
    private String password;
    private String role;
    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String statusCode;
    private String regDate;
    private String birthDate;
    private String messagePermission;
    private String lastOnlineTime;
    private String emojiStatus;
    private String createdOn;
    private String updatedOn;
    private String deletionTimestamp;
    private Boolean deleted;
    private Boolean blocked;
    private Boolean isOnline;

}
