package com.microservice.dto;

import lombok.*;
import com.microservice.model.Role;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMeDto {

    private String id;

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
    private String regDate;
    private String birthDate;
    private String messagePermission;
    private String lastOnlineTime;
    private String emojiStatus;
    private String createdOn;
    private String updatedOn;
    private String deletionTimestamp;
    private boolean deleted;
    private boolean blocked;
    private boolean isOnline;


}
