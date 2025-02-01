package microservice_account.microservice_account.dto;

import lombok.*;
import microservice_account.microservice_account.model.Role;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMeDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;
    private String photo;
    private String profileCover;
    private String about;
    private String city;
    private String country;
    private String statusCode;
    private LocalDateTime regDate;
    private LocalDateTime birthDate;
    private String messagePermission;
    private LocalDateTime lastOnlineTime;
    private String emojiStatus;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private LocalDateTime deletionTimestamp;
    private Boolean deleted;
    private Boolean blocked;
    private Boolean isOnline;

    private Set<Role> roles;

}
